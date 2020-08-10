package com.github.starnowski.posmulten.postgresql.core.functional.tests.combine;

import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Notification;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import org.postgresql.util.PSQLException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractRLSForSingleTableForWhichTenantColumnWasJustAddedTest extends FullStackTest{

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

    @Test(dataProvider = "userData", dependsOnMethods = {"executeSQLDefinitions"}, testName = "insert data into the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to insert data into the users table assigned to the current tenant")
    public void insertDataIntoUserTableAsCurrentTenant(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"insertDataIntoUserTableAsCurrentTenant"}, testName = "try to insert data into the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to insert data into the users table assigned to the different tenant than currently set")
    public void tryToInsertDataIntoNotificationTableAsDifferentTenant(Notification notification, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%7$s INSERT INTO %6$s (uuid, title, content, user_id, tenant) VALUES ('%1$s', '%2$s', '%3$s', %4$d, '%5$s');", notification.getUuid(), notification.getTitle(), notification.getContent(), notification.getUserId(), notification.getTenantId(), getNotificationsTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant)))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToInsertDataIntoNotificationTableAsDifferentTenant"}, testName = "insert data into the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to insert data into the users table assigned to the current tenant")
    public void insertDataIntoNotificationTableAsDifferentTenant(Object[] parameters)
    {
        Notification notification = (Notification) parameters[0];
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%7$s INSERT INTO %6$s (uuid, title, content, user_id, tenant) VALUES ('%1$s', '%2$s', '%3$s', %4$d, '%5$s');", notification.getUuid(), notification.getTitle(), notification.getContent(), notification.getUserId(), notification.getTenantId(), getNotificationsTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(notification.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE uuid = '%1$s' AND title = '%2$s' AND tenant = '%3$s'", notification.getUuid(), notification.getTitle(), notification.getTenantId(), getNotificationsTableReference())), "The tests notification should exists");
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"insertDataIntoNotificationTableAsDifferentTenant"}, testName = "try to select data from the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to select data from the users table assigned to the different tenant than currently set")
    public void tryToSelectDataFromNotificationTableAsDifferentTenant(Notification notification, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
        assertFalse(selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT EXISTS ( SELECT 1 FROM %2$s WHERE uuid = '%1$s' ) ;", notification.getUuid(), getNotificationsTableReference()), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant)), "The SELECT statement should not return any records for different tenant then currently set");
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToSelectDataFromNotificationTableAsDifferentTenant"}, testName = "try to select data from the users table assigned to the current tenant", description = "test case assumes that row level security for users table is not going to allow to select data from the users table assigned to the current tenant")
    public void tryToSelectDataFromNotificationTableAsSameTenant(Object[] parameters)
    {
        Notification notification = (Notification) parameters[0];
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
        assertTrue(selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT EXISTS ( SELECT 1 FROM %2$s WHERE uuid = '%1$s' ) ;", notification.getUuid(), getNotificationsTableReference()), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(notification.getTenantId())), "The SELECT statement should return records for current tenant");
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToSelectDataFromNotificationTableAsSameTenant"}, testName = "try to update data in the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to update data in the users table assigned to the different tenant than currently set")
    public void tryToUpdateDataInNotificationTableAsDifferentTenant(Notification notification, String differentTenant)
    {
        String updatedTitle = "[UPDATE_NAME]" + notification.getTitle();
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), notification.getTitle()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), updatedTitle))).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET title = '%4$s' WHERE uuid = '%3$s' ;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant), getNotificationsTableReference(), notification.getUuid(), updatedTitle));
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), notification.getTitle()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), updatedTitle))).isEqualTo(0);
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToUpdateDataInNotificationTableAsDifferentTenant"}, testName = "update data in the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to update data in the users table assigned to the current tenant")
    public void updateDataInNotificationTableAsDifferentTenant(Object[] parameters)
    {
        Notification notification = (Notification) parameters[0];
        String updatedTitle = "[UPDATE_NAME]" + notification.getTitle();
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), notification.getTitle()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), updatedTitle))).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET title = '%4$s' WHERE uuid = '%3$s' ;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(notification.getTenantId()), getNotificationsTableReference(), notification.getUuid(), updatedTitle));
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), notification.getTitle()))).isEqualTo(0);
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), format("uuid = '%1$s' AND title = '%2$s'", notification.getUuid(), updatedTitle))).isEqualTo(1);
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"updateDataInNotificationTableAsDifferentTenant"}, testName = "try to delete data from the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to delete data from the users table assigned to the different tenant than currently set")
    public void tryToDeleteDataFromNotificationTableAsDifferentTenant(Notification notification, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
        ownerJdbcTemplate.execute(format("%1$s DELETE FROM  %2$s WHERE uuid = '%3$s';", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant), getNotificationsTableReference(), notification.getUuid()));
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
    }

    @Test(dataProvider = "notificationData", dependsOnMethods = {"tryToDeleteDataFromNotificationTableAsDifferentTenant"}, testName = "delete data from the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to delete data from the users table assigned to the current tenant")
    public void deleteDataFromNotificationTableAsDifferentTenant(Object[] parameters)
    {
        Notification notification = (Notification) parameters[0];
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(1);
        ownerJdbcTemplate.execute(format("%1$s DELETE FROM  %2$s WHERE uuid = '%3$s';", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(notification.getTenantId()), getNotificationsTableReference(), notification.getUuid()));
        assertThat(countRowsInTableWhere(getNotificationsTableReference(), "uuid = '" + notification.getUuid() + "'")).isEqualTo(0);
    }

    @Override
    @Test(dependsOnMethods = { "tryToInsertDataIntoNotificationTableAsDifferentTenant", "insertDataIntoNotificationTableAsDifferentTenant", "tryToSelectDataFromNotificationTableAsDifferentTenant", "tryToSelectDataFromNotificationTableAsSameTenant", "tryToUpdateDataInNotificationTableAsDifferentTenant", "updateDataInNotificationTableAsDifferentTenant", "tryToDeleteDataFromNotificationTableAsDifferentTenant", "deleteDataFromNotificationTableAsDifferentTenant" }, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }
}
