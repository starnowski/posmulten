package com.github.starnowski.posmulten.postgresql.core.functional.tests.combine;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.AbstractClassWithSQLDefinitionGenerationMethods;
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
        defaultSharedSchemaContextBuilder.setGrantee(CORE_OWNER_USER);
        defaultSharedSchemaContextBuilder.createRLSPolicyForColumn(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy");
        defaultSharedSchemaContextBuilder.createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME);
        defaultSharedSchemaContextBuilder.createRLSPolicyForColumn(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForColumn(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForColumn(GROUPS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), "tenant_id", "groups_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForColumn(USERS_GROUPS_TABLE_NAME, new HashMap<>(), "tenant_id", "users_groups_table_rls_policy");
        HashMap<String, String> commentsIdColumnNameAndType = new HashMap<>();
        commentsIdColumnNameAndType.put("id", "int");
        commentsIdColumnNameAndType.put("user_id", "bigint");
        defaultSharedSchemaContextBuilder.createRLSPolicyForColumn(COMMENTS_TABLE_NAME, commentsIdColumnNameAndType, CUSTOM_TENANT_COLUMN_NAME, "comments_table_rls_policy");
        AbstractSharedSchemaContext sharedSchemaContext = defaultSharedSchemaContextBuilder.build();

        IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionDefinition = sharedSchemaContext.getIGetCurrentTenantIdFunctionInvocationFactory();
        setCurrentTenantIdFunctionInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
        TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory = sharedSchemaContext.getTenantHasAuthoritiesFunctionInvocationFactory();

        sqlDefinitions.addAll(sharedSchemaContext.getSqlDefinitions());
        // TODO Use the DefaultSharedSchemaContextBuilder to create all SQL definitions

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
