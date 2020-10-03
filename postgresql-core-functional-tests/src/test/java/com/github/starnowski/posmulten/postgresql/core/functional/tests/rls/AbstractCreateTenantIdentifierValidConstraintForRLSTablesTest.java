package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.DefaultTestNGTest;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.Test;

import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.util.Arrays.asList;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractCreateTenantIdentifierValidConstraintForRLSTablesTest extends DefaultTestNGTest {

    protected static final String CUSTOM_TENANT_COLUMN_NAME = "tenant";
    protected static final String FIRST_INVALID_TENANT_IDENTIFIER = "DUMMMY_TENANT";
    protected static final String SECOND_INVALID_TENANT_IDENTIFIER = "XXX-INVAlid_tenant";
    protected static final String DEFAULT_CONSTRAINT_NAME = "tenant_should_be_valid";
    protected static final String POSTS_TABLE_CONSTRAINT_NAME = "posts_tenant_is_valid";

    abstract protected String getSchema();

    protected ISetCurrentTenantIdFunctionInvocationFactory setCurrentTenantIdFunctionInvocationFactory;

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware and also creates column for tenant id")
    public void createSQLDefinitions() throws SharedSchemaContextBuilderException {
        ISharedSchemaContext result = (new DefaultSharedSchemaContextBuilder(getSchema())).setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME)
                .setForceRowLevelSecurityForTableOwner(true)
                .setGrantee(CORE_OWNER_USER)
                .createTenantColumnForTable(NOTIFICATIONS_TABLE_NAME)
                .createRLSPolicyForTable(NOTIFICATIONS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), CUSTOM_TENANT_COLUMN_NAME, "notifications_table_rls_policy")
                .createRLSPolicyForTable(USERS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "users_table_rls_policy")
                .createRLSPolicyForTable(POSTS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("id", "bigint"), "tenant_id", "posts_table_rls_policy")
                .createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), "post_users_tenant_fk")
                .createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), "notification_users_tenant_fk")
                .setNameForFunctionThatChecksIfRecordExistsInTable(USERS_TABLE_NAME, "is_user_belongs_to_current_tenant")
                .createValidTenantValueConstraint(asList(FIRST_INVALID_TENANT_IDENTIFIER, SECOND_INVALID_TENANT_IDENTIFIER), "is_tenant_id_valid", DEFAULT_CONSTRAINT_NAME)
                .registerCustomValidTenantValueConstraintNameForTable(POSTS_TABLE_NAME, POSTS_TABLE_CONSTRAINT_NAME)
                .build();

        sqlDefinitions.addAll(result.getSqlDefinitions());
        setCurrentTenantIdFunctionInvocationFactory = result.getISetCurrentTenantIdFunctionInvocationFactory();
    }

    @Test(dependsOnMethods = {"createSQLDefinitions"}, testName = "constraint should not exists before tests execution", description = "check if constraint does not exist before executing SQL definitions")
    public void constraintShouldNotExistsBeforeTests()
    {
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), POSTS_TABLE_NAME, POSTS_TABLE_CONSTRAINT_NAME)), "Constraint for posts table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), NOTIFICATIONS_TABLE_NAME, DEFAULT_CONSTRAINT_NAME)), "Constraint for notifications table should not exists");
        assertFalse(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), USERS_TABLE_NAME, DEFAULT_CONSTRAINT_NAME)), "Constraint for users table should not exists");
    }

    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    @Test(dependsOnMethods = {"constraintShouldNotExistsBeforeTests"}, testName = "execute SQL definitions")
    public void executeSQLDefinitions()
    {
        super.executeSQLDefinitions();
    }

    @Test(dependsOnMethods = {"executeSQLDefinitions"}, testName = "constraint should exist after SQL definitions executed", description = "check if constraint exist after executing SQL definitions")
    public void constraintNameShouldExistAfterCreation()
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), POSTS_TABLE_NAME, POSTS_TABLE_CONSTRAINT_NAME)), "Constraint for posts table should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), NOTIFICATIONS_TABLE_NAME, DEFAULT_CONSTRAINT_NAME)), "Constraint for notifications table should exists");
        assertTrue(isAnyRecordExists(jdbcTemplate, createSelectStatementForConstraintName(getSchema(), USERS_TABLE_NAME, DEFAULT_CONSTRAINT_NAME)), "Constraint for users table should exists");
    }

    private Map<String, String> prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType)
    {
        return mapBuilder().put(columnName, columnType).build();
    }
}
