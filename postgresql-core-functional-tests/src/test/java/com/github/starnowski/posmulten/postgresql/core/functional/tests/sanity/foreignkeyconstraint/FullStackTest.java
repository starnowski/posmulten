package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity.foreignkeyconstraint;

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.AbstractClassWithSQLDefinitionGenerationMethods;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
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
import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

public abstract class FullStackTest extends AbstractClassWithSQLDefinitionGenerationMethods {

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";
    protected static final String CUSTOM_TENANT_COLUMN_NAME = "tenant";
    protected ISetCurrentTenantIdFunctionInvocationFactory setCurrentTenantIdFunctionInvocationFactory;
    protected ISharedSchemaContext sharedSchemaContext;
    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    protected String getSchema() {
        return "non_public_schema_with_composite_key";
    }

    protected String getUsersTableReference() {
        return (getSchema() == null ? "" : getSchema() + ".") + "users";
    }

    protected String getNotificationsTableReference() {
        return (getSchema() == null ? "" : getSchema() + ".") + "notifications";
    }

    protected String getGroupsTableReference() {
        return (getSchema() == null ? "" : getSchema() + ".") + "groups";
    }

    protected String getUsersGroupsTableReference() {
        return (getSchema() == null ? "" : getSchema() + ".") + "users_groups";
    }

    protected String getGranteeForSharedSchemaContextBuilderInitialization() {
        return CORE_OWNER_USER;
    }

    protected String getSchemaForSharedSchemaContextBuilderInitialization() {
        return getSchema();
    }

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware")
    public void createSQLDefinitions() throws SharedSchemaContextBuilderException {
        DefaultSharedSchemaContextBuilder defaultSharedSchemaContextBuilder = new DefaultSharedSchemaContextBuilder(getSchemaForSharedSchemaContextBuilderInitialization());
        defaultSharedSchemaContextBuilder.setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME);
        defaultSharedSchemaContextBuilder.setForceRowLevelSecurityForTableOwner(true);
        defaultSharedSchemaContextBuilder.setGrantee(getGranteeForSharedSchemaContextBuilderInitialization());
        defaultSharedSchemaContextBuilder.createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME);
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(GROUPS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), "tenant_id", "groups_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(USERS_GROUPS_TABLE_NAME, new HashMap<>(), "tenant_id", "users_groups_table_rls_policy");
        defaultSharedSchemaContextBuilder.createRLSPolicyForTable(COMMENTS_TABLE_NAME, mapBuilder().put("id", "int").put("user_id", "bigint").build(), CUSTOM_TENANT_COLUMN_NAME, "comments_table_rls_policy");
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), POSTS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), COMMENTS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, POSTS_TABLE_NAME, mapBuilder().put("post_id", "id").build(), COMMENTS_POSTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(COMMENTS_TABLE_NAME, COMMENTS_TABLE_NAME, mapBuilder().put("parent_comment_id", "id").put("parent_comment_user_id", "user_id").build(), COMMENTS_PARENT_COMMENTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), NOTIFICATIONS_USERS_COMMENTS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(USERS_GROUPS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), USERS_GROUPS_USERS_FK_CONSTRAINT_NAME);
        defaultSharedSchemaContextBuilder.createSameTenantConstraintForForeignKey(USERS_GROUPS_TABLE_NAME, GROUPS_TABLE_NAME, mapBuilder().put("group_id", "uuid").build(), USERS_GROUPS_GROUPS_FK_CONSTRAINT_NAME);

        this.sharedSchemaContext = defaultSharedSchemaContextBuilder.setCreateForeignKeyConstraintWithTenantColumn(true).build();
        setCurrentTenantIdFunctionInvocationFactory = sharedSchemaContext.getISetCurrentTenantIdFunctionInvocationFactory();
        sqlDefinitions.addAll(sharedSchemaContext.getSqlDefinitions());
    }

    private Map<String, String> prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType) {
        return mapBuilder().put(columnName, columnType).build();
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions() {
        super.executeSQLDefinitions();
    }

    protected TableKey tk(String table) {
        return new TableKey(table, getSchema());
    }
}
