package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.DefaultTestNGTest;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Notification;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Post;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractCreateTenantIdentifierValidConstraintForRLSTablesTest extends DefaultTestNGTest {

    protected static final String CUSTOM_TENANT_COLUMN_NAME = "tenant";
    protected static final String FIRST_INVALID_TENANT_IDENTIFIER = "DUMMMY_TENANT";
    protected static final String SECOND_INVALID_TENANT_IDENTIFIER = "XXX-INVAlid_tenant";
    protected static final String FIRST_VALID_TENANT_IDENTIFIER = "Tenant_id";
    protected static final String SECOND_VALID_TENANT_IDENTIFIER = "correct-af13-sadf";
    protected static final String DEFAULT_CONSTRAINT_NAME = "tenant_should_be_valid";
    protected static final String POSTS_TABLE_CONSTRAINT_NAME = "posts_tenant_is_valid";

    abstract protected String getSchema();

    protected ISetCurrentTenantIdFunctionInvocationFactory setCurrentTenantIdFunctionInvocationFactory;

    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    @DataProvider(name = "userData")
    protected static Object[][] userData()
    {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", FIRST_VALID_TENANT_IDENTIFIER)},
                {new User(2L, "John Doe", SECOND_VALID_TENANT_IDENTIFIER)}
        };
    }

    @DataProvider(name = "postData")
    protected static Object[][] postData()
    {
        return new Object[][]{
                {new Post(57L, "Some phrase", 1L, FIRST_VALID_TENANT_IDENTIFIER)},
                {new Post(73L, "Some text", 2L, SECOND_VALID_TENANT_IDENTIFIER)}
        };
    }

    @DataProvider(name = "notificationData")
    protected static Object[][] notificationData()
    {
        return new Object[][]{
                {new Notification("40e6215d-b5c6-4896-987c-f30f3678f608", "Notification content", "Test", 1L, FIRST_VALID_TENANT_IDENTIFIER)},
                {new Notification("3f333df6-90a4-4fda-8dd3-9485d27cee36", "Notification content", "Test", 2L, SECOND_VALID_TENANT_IDENTIFIER)}
        };
    }

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + USERS_TABLE_NAME;
    }


    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware and also creates column for tenant id")
    public void createSQLDefinitions() throws SharedSchemaContextBuilderException {
        ISharedSchemaContext result = (new DefaultSharedSchemaContextBuilder(getSchema())).setCurrentTenantIdProperty(VALID_CURRENT_TENANT_ID_PROPERTY_NAME)
                .setForceRowLevelSecurityForTableOwner(true)
                .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true)
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
                .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true)
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

    @Test(dataProvider = "userData", dependsOnMethods = {"constraintNameShouldExistAfterCreation"}, testName = "try to insert data into the users table assigned to invalid tenant", description = "test case assumes that row level security for users table is not going to allow to insert data into the users table assigned to invalid tenant")
    public void tryToInsertDataIntoUserTableAsDifferentTenant(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%4$s INSERT INTO %3$s (id, name) VALUES (%1$d, '%2$s');", user.getId(), user.getName(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(FIRST_INVALID_TENANT_IDENTIFIER)))
        ).isInstanceOf(DataIntegrityViolationException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThatThrownBy(() ->
                jdbcTemplate.execute(format("INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), SECOND_INVALID_TENANT_IDENTIFIER, getUsersTableReference()))
        ).isInstanceOf(DataIntegrityViolationException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToInsertDataIntoUserTableAsDifferentTenant"}, testName = "insert data into the users table assigned to valid tenant", description = "test case assumes that row level security for users table is going to allow to insert data into the users table assigned to valid tenant")
    public void insertDataIntoUserTableAsCurrentTenant(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    //TODO add posts
    //TODO add notifications
    //TODO add groups - exclude default value for tenant column

    @Override
    @Test(dependsOnMethods = { "insertDataIntoUserTableAsCurrentTenant" }, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }

    private Map<String, String> prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType)
    {
        return mapBuilder().put(columnName, columnType).build();
    }
}
