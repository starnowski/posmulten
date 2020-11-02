package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.DefaultTestNGTest;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Group;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Notification;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Post;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory;
import org.assertj.core.api.AssertionsForClassTypes;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
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

    @DataProvider(name = "groupsData")
    protected static Object[][] groupsData()
    {
        return new Object[][]{
                {new Group("40e6215d-b5c6-4896-987c-f30f3678f608", "admin_tenant_1", FIRST_VALID_TENANT_IDENTIFIER)},
                {new Group("3f333df6-90a4-4fda-8dd3-9485d27cee36", "admin_tenant_2", SECOND_VALID_TENANT_IDENTIFIER)}
        };
    }

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + USERS_TABLE_NAME;
    }

    protected String getPostsTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + POSTS_TABLE_NAME;
    }

    protected String getNotificationsTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "notifications";
    }

    protected String getGroupsTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "groups";
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
                .createRLSPolicyForTable(GROUPS_TABLE_NAME, prepareIdColumnTypeForSingleColumnKey("uuid", "uuid"), "tenant_id", "groups_table_rls_policy")
                .createSameTenantConstraintForForeignKey(POSTS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), "post_users_tenant_fk")
                .createSameTenantConstraintForForeignKey(NOTIFICATIONS_TABLE_NAME, USERS_TABLE_NAME, mapBuilder().put("user_id", "id").build(), "notification_users_tenant_fk")
                .setNameForFunctionThatChecksIfRecordExistsInTable(USERS_TABLE_NAME, "is_user_belongs_to_current_tenant")
                .createValidTenantValueConstraint(asList(FIRST_INVALID_TENANT_IDENTIFIER, SECOND_INVALID_TENANT_IDENTIFIER), "is_tenant_id_valid", DEFAULT_CONSTRAINT_NAME)
                .registerCustomValidTenantValueConstraintNameForTable(POSTS_TABLE_NAME, POSTS_TABLE_CONSTRAINT_NAME)
                .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true)
                .skipAddingOfTenantColumnDefaultValueForTable(GROUPS_TABLE_NAME)
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

    @Test(dataProvider = "userData", dependsOnMethods = {"constraintNameShouldExistAfterCreation"}, testName = "try to insert data into the users table assigned to invalid tenant by default value statement", description = "test case assumes that constraint that check if tenant value is correct for users table is not going to allow to insert data into the users table  when tenant value comes from default value statement and current tenant has invalid value")
    public void tryToInsertDataIntoUserTableWithInvalidTenantByDefaultValueStatement(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%4$s INSERT INTO %3$s (id, name) VALUES (%1$d, '%2$s');", user.getId(), user.getName(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(FIRST_INVALID_TENANT_IDENTIFIER)))
        ).isInstanceOf(DataIntegrityViolationException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
    }

    //TODO Description
    @Test(dataProvider = "userData", dependsOnMethods = {"constraintNameShouldExistAfterCreation"}, testName = "try to insert data into the users table assigned to invalid tenant", description = "test case assumes that row level security for users table is not going to allow to insert data into the users table assigned to invalid tenant")
    public void tryToInsertDataIntoUserTableWithInvalidTenantBySpecifiedValueInInsertStatement(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        assertThatThrownBy(() ->
                jdbcTemplate.execute(format("INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), SECOND_INVALID_TENANT_IDENTIFIER, getUsersTableReference()))
        ).isInstanceOf(DataIntegrityViolationException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
    }

    //TODO Description
    @Test(dataProvider = "userData", dependsOnMethods = {"tryToInsertDataIntoUserTableWithInvalidTenantBySpecifiedValueInInsertStatement"}, testName = "insert data into the users table assigned to valid tenant", description = "test case assumes that row level security for users table is going to allow to insert data into the users table assigned to valid tenant")
    public void insertDataIntoUserTableAsCurrentTenant(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    //TODO Description
    @Test(dataProvider = "postData", dependsOnMethods = {"insertDataIntoUserTableAsCurrentTenant"}, testName = "try to insert data into the post table related to primary tests tenant as different tenant", description = "test case assumes that constraint is not going to allow to insert data into the post table related to the primary tenant when a current tenant is different")
    public void tryToInsertPostTableWithInvalidTenant(Post post)
    {
        assertThat(countRowsInTableWhere(getPostsTableReference(), "id = " + post.getUserId())).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%1$s INSERT INTO %5$s (id, user_id, text) VALUES (%2$d, %3$d, '%4$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(FIRST_INVALID_TENANT_IDENTIFIER), post.getId(), post.getUserId(), post.getText(), getPostsTableReference()))
        )
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(countRowsInTableWhere(getPostsTableReference(), "id = " + post.getUserId())).isEqualTo(0);
    }

    //TODO Description
    @Test(dataProvider = "postData", dependsOnMethods = {"tryToInsertPostTableWithInvalidTenant"}, testName = "insert data into the post table related to primary tests tenant as the primary tenant")
    public void insertPostForUserFromSameTenant(Post post)
    {
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %3$s WHERE id = %1$d AND tenant_id = '%2$s'", post.getUserId(), post.getTenantId(), getUsersTableReference())), "The tests user should exists");
        assertThat(countRowsInTableWhere(getPostsTableReference(), "id = " + post.getUserId())).isEqualTo(0);
        jdbcTemplate.execute(format("%1$s INSERT INTO %6$s (id, user_id, text, tenant_id) VALUES (%2$d, %3$d, '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(post.getTenantId()), post.getId(), post.getUserId(), post.getText(), post.getTenantId(), getPostsTableReference()));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND text = '%2$s' AND tenant_id = '%3$s'", post.getId(), post.getText(), post.getTenantId(), getPostsTableReference())), "The tests post should exists");
    }
    //TODO add notifications (new created tenant column)
    //TODO Description
    @Test(dataProvider = "notificationData", dependsOnMethods = {"insertPostForUserFromSameTenant"}, testName = "try to insert data into the notifications table assigned to the different tenant than currently set", description = "test case assumes that row level security for notifications table is not going to allow to insert data into the notifications table assigned to the different tenant than currently set")
    public void tryToInsertDataIntoNotificationTableAsDifferentTenant(Notification notification)
    {
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%7$s INSERT INTO %6$s (uuid, title, content, user_id, tenant) VALUES ('%1$s', '%2$s', '%3$s', %4$d, '%5$s');", notification.getUuid(), notification.getTitle(), notification.getContent(), notification.getUserId(), notification.getTenantId(), getNotificationsTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(FIRST_INVALID_TENANT_IDENTIFIER)))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
    }

    //TODO Description
    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToInsertDataIntoNotificationTableAsDifferentTenant"}, testName = "insert data into the notifications table assigned to the currently set", description = "test case assumes that row level security for notifications table is going to allow to insert data into the notifications table assigned to the current tenant")
    public void insertDataIntoNotificationTableAsDifferentTenant(Notification notification)
    {
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%7$s INSERT INTO %6$s (uuid, title, content, user_id, tenant) VALUES ('%1$s', '%2$s', '%3$s', %4$d, '%5$s');", notification.getUuid(), notification.getTitle(), notification.getContent(), notification.getUserId(), notification.getTenantId(), getNotificationsTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(notification.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE uuid = '%1$s' AND title = '%2$s' AND tenant = '%3$s'", notification.getUuid(), notification.getTitle(), notification.getTenantId(), getNotificationsTableReference())), "The tests notification should exists");
    }
    //TODO add groups - exclude default value for tenant column
    //TODO add group with non-null tenant
    //TODO try to add group with null tenant
    @Test(dataProvider = "groupsData", dependsOnMethods = {"insertDataIntoNotificationTableAsDifferentTenant"}, testName = "try to insert data into the groups table assigned to the different tenant than currently set", description = "test case assumes that row level security for groups table is not going to allow to insert data into the groups table assigned to the different tenant than currently set")
    public void tryToInsertDataIntoGroupTableAsDifferentTenant(Group group)
    {
        AssertionsForClassTypes.assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%1$s INSERT INTO %2$s (uuid, name, tenant_id) VALUES ('%3$s', '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(SECOND_INVALID_TENANT_IDENTIFIER), getGroupsTableReference(), group.getUuid(), group.getName(), group.getTenantId()))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        AssertionsForClassTypes.assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(0);
    }

    //TODO Description
    @Test(dataProvider = "groupsData", dependsOnMethods = {"insertDataIntoNotificationTableAsDifferentTenant"}, testName = "try to insert data into the groups table assigned to the different tenant than currently set", description = "test case assumes that row level security for groups table is not going to allow to insert data into the groups table assigned to the different tenant than currently set")
    public void tryToInsertDataIntoGroupTableWithValidTenantButWithoutTenantColumnDefaultValue(Group group)
    {
        AssertionsForClassTypes.assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%1$s INSERT INTO %2$s (uuid, name) VALUES ('%3$s', '%4$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(group.getTenantId()), getGroupsTableReference(), group.getUuid(), group.getName()))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        AssertionsForClassTypes.assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(0);
    }

    //TODO Description
    @Test(dataProvider = "groupsData", dependsOnMethods = {"tryToInsertDataIntoGroupTableWithValidTenantButWithoutTenantColumnDefaultValue"}, testName = "insert data into the groups table assigned to the currently set", description = "test case assumes that row level security for groups table is going to allow to insert data into the groups table assigned to the currently set")
    public void insertDataIntoGroupTableAsCurrentTenant(Group group)
    {
        AssertionsForClassTypes.assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s INSERT INTO %2$s (uuid, name, tenant_id) VALUES ('%3$s', '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(group.getTenantId()), getGroupsTableReference(), group.getUuid(), group.getName(), group.getTenantId()));
        AssertionsForClassTypes.assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(1);
    }

    @Override
    @Test(dependsOnMethods = { "insertDataIntoUserTableAsCurrentTenant", "insertPostForUserFromSameTenant", "insertDataIntoNotificationTableAsDifferentTenant", "insertDataIntoGroupTableAsCurrentTenant"}, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }

    private Map<String, String> prepareIdColumnTypeForSingleColumnKey(String columnName, String columnType)
    {
        return mapBuilder().put(columnName, columnType).build();
    }
}
