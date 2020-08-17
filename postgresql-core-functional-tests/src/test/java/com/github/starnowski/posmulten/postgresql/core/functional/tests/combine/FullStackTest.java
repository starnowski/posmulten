package com.github.starnowski.posmulten.postgresql.core.functional.tests.combine;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.AbstractClassWithSQLDefinitionGenerationMethods;
import com.github.starnowski.posmulten.postgresql.core.rls.EnableRowLevelSecurityProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.ForceRowLevelSecurityProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSPolicyProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultRLSPolicyProducerParameters.builder;
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.ALL;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

public abstract class FullStackTest extends AbstractClassWithSQLDefinitionGenerationMethods {

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";
    protected static final String CUSTOM_TENANT_COLUMN_NAME = "tenant";

    abstract protected String getSchema();

    protected ISetCurrentTenantIdFunctionInvocationFactory setCurrentTenantIdFunctionInvocationFactory;

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "users";
    }
    protected String getNotificationsTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "notifications";
    }

    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware")
    public void createSQLDefinitions()
    {
        DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(getSchema());
        defaultSharedSchemaContextBuilder.setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME);
        defaultSharedSchemaContextBuilder.setForceRowLevelSecurityForTableOwner(true);
        defaultSharedSchemaContextBuilder.createRLSPolicyForColumn(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy");
        defaultSharedSchemaContextBuilder.createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME);
        AbstractSharedSchemaContext sharedSchemaContext = defaultSharedSchemaContextBuilder.build();

        IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionDefinition = sharedSchemaContext.getIGetCurrentTenantIdFunctionInvocationFactory();
        setCurrentTenantIdFunctionInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
        TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory = sharedSchemaContext.getTenantHasAuthoritiesFunctionInvocationFactory();

        sqlDefinitions.addAll(sharedSchemaContext.getSqlDefinitions());
        // TODO Use the DefaultSharedSchemaContextBuilder to create all SQL definitions

        // RLS - users
        // EnableRowLevelSecurityProducer
        EnableRowLevelSecurityProducer enableRowLevelSecurityProducer = new EnableRowLevelSecurityProducer();
        sqlDefinitions.add(enableRowLevelSecurityProducer.produce(USERS_TABLE_NAME, getSchema()));

        // ForceRowLevelSecurityProducer - forcing the row level security policy for table owner
        ForceRowLevelSecurityProducer forceRowLevelSecurityProducer = new ForceRowLevelSecurityProducer();
        sqlDefinitions.add(forceRowLevelSecurityProducer.produce(USERS_TABLE_NAME, getSchema()));

        // RLSPolicyProducer
        RLSPolicyProducer rlsPolicyProducer = new RLSPolicyProducer();
        SQLDefinition usersRLSPolicySQLDefinition = rlsPolicyProducer.produce(builder().withPolicyName("users_table_rls_policy")
                .withPolicySchema(getSchema())
                .withPolicyTable(USERS_TABLE_NAME)
                .withGrantee(CORE_OWNER_USER)
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .build());
        sqlDefinitions.add(usersRLSPolicySQLDefinition);

        // RLS - notifications
        // Enable Row Level Security for notifications table
        sqlDefinitions.add(enableRowLevelSecurityProducer.produce(NOTIFICATIONS_TABLE_NAME, getSchema()));

        // forcing the row level security policy for notifications table
        sqlDefinitions.add(forceRowLevelSecurityProducer.produce(NOTIFICATIONS_TABLE_NAME, getSchema()));

        // Adding RLS for the notifications table
        SQLDefinition notificationsRLSPolicySQLDefinition = rlsPolicyProducer.produce(builder().withPolicyName("notifications_table_rls_policy")
                .withPolicySchema(getSchema())
                .withPolicyTable(NOTIFICATIONS_TABLE_NAME)
                .withGrantee(CORE_OWNER_USER)
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .withTenantIdColumn(CUSTOM_TENANT_COLUMN_NAME)
                .build());
        sqlDefinitions.add(notificationsRLSPolicySQLDefinition);

        // RLS - posts
        // Enable Row Level Security for posts table
        sqlDefinitions.add(enableRowLevelSecurityProducer.produce(POSTS_TABLE_NAME, getSchema()));

        // forcing the row level security policy for posts table
        sqlDefinitions.add(forceRowLevelSecurityProducer.produce(POSTS_TABLE_NAME, getSchema()));

        // Adding RLS for the posts table
        SQLDefinition postsRLSPolicySQLDefinition = rlsPolicyProducer.produce(builder().withPolicyName("posts_table_rls_policy")
                .withPolicySchema(getSchema())
                .withPolicyTable(POSTS_TABLE_NAME)
                .withGrantee(CORE_OWNER_USER)
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .build());
        sqlDefinitions.add(postsRLSPolicySQLDefinition);

        // RLS - groups
        // Enable Row Level Security for groups table
        sqlDefinitions.add(enableRowLevelSecurityProducer.produce(GROUPS_TABLE_NAME, getSchema()));

        // forcing the row level security policy for groups table
        sqlDefinitions.add(forceRowLevelSecurityProducer.produce(GROUPS_TABLE_NAME, getSchema()));

        // Adding RLS for the groups table
        SQLDefinition groupsRLSPolicySQLDefinition = rlsPolicyProducer.produce(builder().withPolicyName("groups_table_rls_policy")
                .withPolicySchema(getSchema())
                .withPolicyTable(GROUPS_TABLE_NAME)
                .withGrantee(CORE_OWNER_USER)
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .build());
        sqlDefinitions.add(groupsRLSPolicySQLDefinition);

        // RLS - users_groups
        // Enable Row Level Security for users_groups table
        sqlDefinitions.add(enableRowLevelSecurityProducer.produce(USERS_GROUPS_TABLE_NAME, getSchema()));

        // forcing the row level security policy for groups table
        sqlDefinitions.add(forceRowLevelSecurityProducer.produce(USERS_GROUPS_TABLE_NAME, getSchema()));

        // Adding RLS for the groups table
        SQLDefinition usersGroupsRLSPolicySQLDefinition = rlsPolicyProducer.produce(builder().withPolicyName("users_groups_table_rls_policy")
                .withPolicySchema(getSchema())
                .withPolicyTable(USERS_GROUPS_TABLE_NAME)
                .withGrantee(CORE_OWNER_USER)
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .build());
        sqlDefinitions.add(usersGroupsRLSPolicySQLDefinition);

        // RLS - comments
        // Enable Row Level Security for comments table
        sqlDefinitions.add(enableRowLevelSecurityProducer.produce(COMMENTS_TABLE_NAME, getSchema()));

        // forcing the row level security policy for groups table
        sqlDefinitions.add(forceRowLevelSecurityProducer.produce(COMMENTS_TABLE_NAME, getSchema()));

        // Adding RLS for the groups table
        SQLDefinition commentsGroupsRLSPolicySQLDefinition = rlsPolicyProducer.produce(builder().withPolicyName("comments_table_rls_policy")
                .withPolicySchema(getSchema())
                .withPolicyTable(COMMENTS_TABLE_NAME)
                .withGrantee(CORE_OWNER_USER)
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                .withTenantIdColumn(CUSTOM_TENANT_COLUMN_NAME)
                .build());
        sqlDefinitions.add(commentsGroupsRLSPolicySQLDefinition);

        // Does record belongs to current tenant (users table)
        IsRecordBelongsToCurrentTenantFunctionDefinition isUsersRecordBelongsToCurrentTenantFunctionDefinition = getIsUsersRecordBelongsToCurrentTenantFunctionDefinition(getCurrentTenantIdFunctionDefinition);
        sqlDefinitions.add(isUsersRecordBelongsToCurrentTenantFunctionDefinition);

        // Does record belongs to current tenant (posts table)
        IsRecordBelongsToCurrentTenantFunctionDefinition isPostsRecordBelongsToCurrentTenantFunctionDefinition = getIsPostsRecordBelongsToCurrentTenantFunctionDefinition(getCurrentTenantIdFunctionDefinition);
        sqlDefinitions.add(isPostsRecordBelongsToCurrentTenantFunctionDefinition);

        // Does record belongs to current tenant (comments table)
        IsRecordBelongsToCurrentTenantFunctionDefinition isCommentsRecordBelongsToCurrentTenantFunctionDefinition = getIsCommentsRecordBelongsToCurrentTenantFunctionDefinition(getCurrentTenantIdFunctionDefinition);
        sqlDefinitions.add(isCommentsRecordBelongsToCurrentTenantFunctionDefinition);

        // Does record belongs to current tenant (notifications table)
        IsRecordBelongsToCurrentTenantFunctionDefinition isNotificationsRecordBelongsToCurrentTenantFunctionDefinition = getIsNotificationsRecordBelongsToCurrentTenantFunctionDefinition(getCurrentTenantIdFunctionDefinition);
        sqlDefinitions.add(isNotificationsRecordBelongsToCurrentTenantFunctionDefinition);

        // Does record belongs to current tenant (groups table)
        IsRecordBelongsToCurrentTenantFunctionDefinition isGroupsRecordBelongsToCurrentTenantFunctionDefinition = getIsGroupsRecordBelongsToCurrentTenantFunctionDefinition(getCurrentTenantIdFunctionDefinition);
        sqlDefinitions.add(isGroupsRecordBelongsToCurrentTenantFunctionDefinition);

        // Constraint - post - fk - users
        //user_id
        SQLDefinition recordBelongsToCurrentTenantConstrainSqlDefinition = getSqlDefinitionOfConstraintForUsersForeignKeyInPostsTable(isUsersRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(recordBelongsToCurrentTenantConstrainSqlDefinition);

        //getSqlDefinitionOfConstraintForUsersForeignKeyInCommentsTable
        SQLDefinition usersBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition = getSqlDefinitionOfConstraintForUsersForeignKeyInCommentsTable(isUsersRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(usersBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition);

        //getSqlDefinitionOfConstraintForPostsForeignKeyInCommentsTable commets - posts fk
        SQLDefinition postsBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition = getSqlDefinitionOfConstraintForPostsForeignKeyInCommentsTable(isPostsRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(postsBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition);

        //getSqlDefinitionOfConstraintForParentCommentForeignKeyInCommentsTable comments - parent comment fk
        SQLDefinition parentCommentBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition = getSqlDefinitionOfConstraintForParentCommentForeignKeyInCommentsTable(isCommentsRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(parentCommentBelongsToCurrentTenantConstraintForCommentsTableSqlDefinition);

        SQLDefinition userBelongsToCurrentTenantConstraintForNotificationsTableSqlDefinition = getSqlDefinitionOfConstraintForUsersForeignKeyInNotificationsTable(isUsersRecordBelongsToCurrentTenantFunctionDefinition);
        sqlDefinitions.add(userBelongsToCurrentTenantConstraintForNotificationsTableSqlDefinition);
    }

    private Map<String, String> prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType)
    {
        Map<String, String> map = new HashMap<>();
        map.put(columnName, columnType);
        return map;
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions()
    {
        super.executeSQLDefinitions();
    }

}
