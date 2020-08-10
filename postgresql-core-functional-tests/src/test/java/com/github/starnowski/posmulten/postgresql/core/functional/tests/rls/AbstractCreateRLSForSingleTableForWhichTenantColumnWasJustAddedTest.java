package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

import com.github.starnowski.posmulten.postgresql.core.*;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.TestNGSpringContextWithoutGenericTransactionalSupportTests;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Notification;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import com.github.starnowski.posmulten.postgresql.core.rls.EnableRowLevelSecurityProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.ForceRowLevelSecurityProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSPolicyProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultRLSPolicyProducerParameters.builder;
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.ALL;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.VALID_CURRENT_TENANT_ID_PROPERTY_NAME;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractCreateRLSForSingleTableForWhichTenantColumnWasJustAddedTest extends TestNGSpringContextWithoutGenericTransactionalSupportTests {

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";
    protected static final String TENANT_COLUMN_NAME = "tenant";

    abstract protected String getSchema();

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "users";
    }
    protected String getNotificationsTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "notifications";
    }

    protected SetCurrentTenantIdFunctionDefinition setCurrentTenantIdFunctionDefinition;

    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    @DataProvider(name = "userData")
    protected static Object[][] userData()
    {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", USER_TENANT)},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT)}
        };
    }

    @DataProvider(name = "notificationData")
    protected static Object[][] notificationData()
    {
        return new Object[][]{
                {new Notification("40e6215d-b5c6-4896-987c-f30f3678f608", "Notification content", "Test", 1L, USER_TENANT), SECONDARY_USER_TENANT},
                {new Notification("3f333df6-90a4-4fda-8dd3-9485d27cee36", "Notification content", "Test", 2L, SECONDARY_USER_TENANT), USER_TENANT}
        };
    }

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware and also creates column for tenant id")
    public void createSQLDefinitions()
    {
        //Create function that returns current tenant function
        GetCurrentTenantIdFunctionProducer getCurrentTenantIdFunctionProducer = new GetCurrentTenantIdFunctionProducer();
        GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition = getCurrentTenantIdFunctionProducer.produce(new GetCurrentTenantIdFunctionProducerParameters("rls_get_current_tenant", VALID_CURRENT_TENANT_ID_PROPERTY_NAME, getSchema(), null));
        sqlDefinitions.add(getCurrentTenantIdFunctionDefinition);

        //Create function that sets current tenant function
        SetCurrentTenantIdFunctionProducer setCurrentTenantIdFunctionProducer = new SetCurrentTenantIdFunctionProducer();
        setCurrentTenantIdFunctionDefinition = setCurrentTenantIdFunctionProducer.produce(new SetCurrentTenantIdFunctionProducerParameters("rls_set_current_tenant", VALID_CURRENT_TENANT_ID_PROPERTY_NAME, getSchema(), null));
        sqlDefinitions.add(setCurrentTenantIdFunctionDefinition);

        // EnableRowLevelSecurityProducer
        EnableRowLevelSecurityProducer enableRowLevelSecurityProducer = new EnableRowLevelSecurityProducer();
        sqlDefinitions.add(enableRowLevelSecurityProducer.produce("users", getSchema()));

        // ForceRowLevelSecurityProducer - forcing the row level security policy for table owner
        ForceRowLevelSecurityProducer forceRowLevelSecurityProducer = new ForceRowLevelSecurityProducer();
        sqlDefinitions.add(forceRowLevelSecurityProducer.produce("users", getSchema()));

        // EqualsCurrentTenantIdentifierFunctionProducer
        EqualsCurrentTenantIdentifierFunctionProducer equalsCurrentTenantIdentifierFunctionProducer = new EqualsCurrentTenantIdentifierFunctionProducer();
        EqualsCurrentTenantIdentifierFunctionDefinition equalsCurrentTenantIdentifierFunctionDefinition = equalsCurrentTenantIdentifierFunctionProducer.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters("is_id_equals_current_tenant_id", getSchema(), null, getCurrentTenantIdFunctionDefinition));
        sqlDefinitions.add(equalsCurrentTenantIdentifierFunctionDefinition);

        // TenantHasAuthoritiesFunctionProducer
        TenantHasAuthoritiesFunctionProducer tenantHasAuthoritiesFunctionProducer = new TenantHasAuthoritiesFunctionProducer();
        TenantHasAuthoritiesFunctionDefinition tenantHasAuthoritiesFunctionDefinition = tenantHasAuthoritiesFunctionProducer.produce(new TenantHasAuthoritiesFunctionProducerParameters("tenant_has_authorities", getSchema(), equalsCurrentTenantIdentifierFunctionDefinition));
        sqlDefinitions.add(tenantHasAuthoritiesFunctionDefinition);

        // RLSPolicyProducer
        RLSPolicyProducer rlsPolicyProducer = new RLSPolicyProducer();
        SQLDefinition usersRLSPolicySQLDefinition = rlsPolicyProducer.produce(builder().withPolicyName("users_table_rls_policy")
                .withPolicySchema(getSchema())
                .withPolicyTable("users")
                .withGrantee(CORE_OWNER_USER)
                .withPermissionCommandPolicy(ALL)
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionDefinition)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionDefinition)
                .build());
        sqlDefinitions.add(usersRLSPolicySQLDefinition);

        // Create tenant column in the notifications table
        CreateColumnStatementProducer createColumnStatementProducer = new CreateColumnStatementProducer();
        sqlDefinitions.add(createColumnStatementProducer.produce(new CreateColumnStatementProducerParameters(NOTIFICATIONS_TABLE_NAME, TENANT_COLUMN_NAME, "character varying(255)", getSchema())));

        // Setting default value
        SetDefaultStatementProducer setDefaultStatementProducer = new SetDefaultStatementProducer();
        sqlDefinitions.add(setDefaultStatementProducer.produce(new SetDefaultStatementProducerParameters(NOTIFICATIONS_TABLE_NAME, TENANT_COLUMN_NAME, getCurrentTenantIdFunctionDefinition.returnGetCurrentTenantIdFunctionInvocation(), getSchema())));

        // Setting NOT NULL declaration
        SetNotNullStatementProducer setNotNullStatementProducer = new SetNotNullStatementProducer();
        sqlDefinitions.add(setNotNullStatementProducer.produce(new SetNotNullStatementProducerParameters(NOTIFICATIONS_TABLE_NAME, TENANT_COLUMN_NAME, getSchema())));

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
                .withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionDefinition)
                .withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionDefinition)
                .withTenantIdColumn(TENANT_COLUMN_NAME)
                .build());
        sqlDefinitions.add(notificationsRLSPolicySQLDefinition);
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

    @Test(dataProvider = "userData", dependsOnMethods = {"executeSQLDefinitions"}, testName = "insert data into the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to insert data into the users table assigned to the current tenant")
    public void insertDataIntoUserTableAsCurrentTenant(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"insertDataIntoUserTableAsCurrentTenant"}, testName = "try to insert data into the notifications table assigned to the different tenant than currently set", description = "test case assumes that row level security for notifications table is not going to allow to insert data into the notifications table assigned to the different tenant than currently set")
    public void tryToInsertDataIntoNotificationTableAsDifferentTenant(Notification notification, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%7$s INSERT INTO %6$s (uuid, title, content, user_id, tenant) VALUES ('%1$s', '%2$s', '%3$s', %4$d, '%5$s');", notification.getUuid(), notification.getTitle(), notification.getContent(), notification.getUserId(), notification.getTenantId(), getNotificationsTableReference(), setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(differentTenant)))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToInsertDataIntoNotificationTableAsDifferentTenant"}, testName = "insert data into the notifications table assigned to the currently set", description = "test case assumes that row level security for notifications table is going to allow to insert data into the notifications table assigned to the current tenant")
    public void insertDataIntoNotificationTableAsDifferentTenant(Object[] parameters)
    {
        Notification notification = (Notification) parameters[0];
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%7$s INSERT INTO %6$s (uuid, title, content, user_id, tenant) VALUES ('%1$s', '%2$s', '%3$s', %4$d, '%5$s');", notification.getUuid(), notification.getTitle(), notification.getContent(), notification.getUserId(), notification.getTenantId(), getNotificationsTableReference(), setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(notification.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE uuid = '%1$s' AND title = '%2$s' AND tenant = '%3$s'", notification.getUuid(), notification.getTitle(), notification.getTenantId(), getNotificationsTableReference())), "The tests notification should exists");
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"insertDataIntoNotificationTableAsDifferentTenant"}, testName = "try to select data from the notifications table assigned to the different tenant than currently set", description = "test case assumes that row level security for notifications table is not going to allow to select data from the users table assigned to the different tenant than currently set")
    public void tryToSelectDataFromNotificationTableAsDifferentTenant(Notification notification, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
        assertFalse(selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT EXISTS ( SELECT 1 FROM %2$s WHERE uuid = '%1$s' ) ;", notification.getUuid(), getNotificationsTableReference()), setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(differentTenant)), "The SELECT statement should not return any records for different tenant then currently set");
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToSelectDataFromNotificationTableAsDifferentTenant"}, testName = "try to select data from the notifications table assigned to the current tenant", description = "test case assumes that row level security for notifications table is not going to allow to select data from the notifications table assigned to the current tenant")
    public void tryToSelectDataFromNotificationTableAsSameTenant(Object[] parameters)
    {
        Notification notification = (Notification) parameters[0];
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
        assertTrue(selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT EXISTS ( SELECT 1 FROM %2$s WHERE uuid = '%1$s' ) ;", notification.getUuid(), getNotificationsTableReference()), setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(notification.getTenantId())), "The SELECT statement should return records for current tenant");
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToSelectDataFromNotificationTableAsSameTenant"}, testName = "try to update data in the notifications table assigned to the different tenant than currently set", description = "test case assumes that row level security for notifications table is not going to allow to update data in the notifications table assigned to the different tenant than currently set")
    public void tryToUpdateDataInNotificationTableAsDifferentTenant(Notification notification, String differentTenant)
    {
        String updatedTitle = "[UPDATE_NAME]" + notification.getTitle();
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), notification.getTitle()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), updatedTitle))).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET title = '%4$s' WHERE uuid = '%3$s' ;", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(differentTenant), getNotificationsTableReference(), notification.getUuid(), updatedTitle));
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), notification.getTitle()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), updatedTitle))).isEqualTo(0);
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToUpdateDataInNotificationTableAsDifferentTenant"}, testName = "update data in the notifications table assigned to the currently set", description = "test case assumes that row level security for notifications table is going to allow to update data in the notifications table assigned to the current tenant")
    public void updateDataInNotificationTableAsDifferentTenant(Object[] parameters)
    {
        Notification notification = (Notification) parameters[0];
        String updatedTitle = "[UPDATE_NAME]" + notification.getTitle();
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), notification.getTitle()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), updatedTitle))).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET title = '%4$s' WHERE uuid = '%3$s' ;", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(notification.getTenantId()), getNotificationsTableReference(), notification.getUuid(), updatedTitle));
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), notification.getTitle()))).isEqualTo(0);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), updatedTitle))).isEqualTo(1);
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"updateDataInNotificationTableAsDifferentTenant"}, testName = "try to delete data from the notifications table assigned to the different tenant than currently set", description = "test case assumes that row level security for notifications table is not going to allow to delete data from the notifications table assigned to the different tenant than currently set")
    public void tryToDeleteDataFromNotificationTableAsDifferentTenant(Notification notification, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
        ownerJdbcTemplate.execute(format("%1$s DELETE FROM  %2$s WHERE uuid = '%3$s';", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(differentTenant), getNotificationsTableReference(), notification.getUuid()));
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToDeleteDataFromNotificationTableAsDifferentTenant"}, testName = "delete data from the notifications table assigned to the currently set", description = "test case assumes that row level security for notifications table is going to allow to delete data from the notifications table assigned to the current tenant")
    public void deleteDataFromNotificationTableAsDifferentTenant(Object[] parameters)
    {
        Notification notification = (Notification) parameters[0];
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
        ownerJdbcTemplate.execute(format("%1$s DELETE FROM  %2$s WHERE uuid = '%3$s';", setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(notification.getTenantId()), getNotificationsTableReference(), notification.getUuid()));
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
    }

    @Override
    @Test(dependsOnMethods = { "tryToInsertDataIntoNotificationTableAsDifferentTenant", "insertDataIntoNotificationTableAsDifferentTenant", "tryToSelectDataFromNotificationTableAsDifferentTenant", "tryToSelectDataFromNotificationTableAsSameTenant", "tryToUpdateDataInNotificationTableAsDifferentTenant", "updateDataInNotificationTableAsDifferentTenant", "tryToDeleteDataFromNotificationTableAsDifferentTenant", "deleteDataFromNotificationTableAsDifferentTenant" }, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }
}
