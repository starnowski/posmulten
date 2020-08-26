package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public abstract class AbstractRLSForSingleTableTest extends FullStackTest{

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";

    abstract protected String getSchema();

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "users";
    }

    @Autowired
    @Qualifier("ownerJdbcTemplate")
    protected JdbcTemplate ownerJdbcTemplate;

    @DataProvider(name = "userData")
    protected static Object[][] userData()
    {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", USER_TENANT), SECONDARY_USER_TENANT},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT), USER_TENANT}
        };
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"executeSQLDefinitions"}, testName = "try to insert data into the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to insert data into the users table assigned to the different tenant than currently set")
    public void tryToInsertDataIntoUserTableAsDifferentTenant(User user, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant)))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToInsertDataIntoUserTableAsDifferentTenant"}, testName = "insert data into the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to insert data into the users table assigned to the current tenant")
    public void insertDataIntoUserTableAsCurrentTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"insertDataIntoUserTableAsCurrentTenant"}, testName = "try to select data from the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to select data from the users table assigned to the different tenant than currently set")
    public void tryToSelectDataFromUserTableAsDifferentTenant(User user, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
        assertFalse(selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT EXISTS ( SELECT 1 FROM %2$s WHERE id = %1$d ) ;", user.getId(), getUsersTableReference()), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant)), "The SELECT statement should not return any records for different tenant then currently set");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToSelectDataFromUserTableAsDifferentTenant"}, testName = "try to select data from the users table assigned to the current tenant", description = "test case assumes that row level security for users table is not going to allow to select data from the users table assigned to the current tenant")
    public void tryToSelectDataFromUserTableAsSameTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
        assertTrue(selectAndReturnFirstRecordAsBooleanWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT EXISTS ( SELECT 1 FROM %2$s WHERE id = %1$d ) ;", user.getId(), getUsersTableReference()), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())), "The SELECT statement should return records for current tenant");
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToSelectDataFromUserTableAsSameTenant"},  testName = "counting statement should only proceed records that belongs to current tenant", description = "test case assumes that row level security for users table will cause that the counting statement should only proceed records that belongs to current tenant")
    public void selectAllShouldReturnOnlyRecordsThatBelongsToCurrentTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        int numberOfAllRecordsInUserTable = countRowsInTable(getUsersTableReference());
        Long numberOfAllRecordsInUserTableForCurrentTenant = selectAndReturnFirstRecordAsLongWithSettingCurrentTenantId(ownerJdbcTemplate, format("SELECT COUNT(0) FROM %1$s;", getUsersTableReference()), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId()));
        assertThat(numberOfAllRecordsInUserTableForCurrentTenant).isGreaterThan(0).isLessThan(numberOfAllRecordsInUserTable);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"selectAllShouldReturnOnlyRecordsThatBelongsToCurrentTenant"}, testName = "try to update data in the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to update data in the users table assigned to the different tenant than currently set")
    public void tryToUpdateDataInUserTableAsDifferentTenant(User user, String differentTenant)
    {
        String updatedName = "[UPDATE_NAME]" + user.getName();
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), user.getName()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), updatedName))).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET name = '%4$s' WHERE id = %3$d ;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant), getUsersTableReference(), user.getId(), updatedName));
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), user.getName()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), updatedName))).isEqualTo(0);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToUpdateDataInUserTableAsDifferentTenant"}, testName = "update data in the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to update data in the users table assigned to the current tenant")
    public void updateDataInUserTableAsDifferentTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        String updatedName = "[UPDATE_NAME]" + user.getName();
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), user.getName()))).isEqualTo(1);
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), updatedName))).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s UPDATE %2$s SET name = '%4$s' WHERE id = %3$d ;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId()), getUsersTableReference(), user.getId(), updatedName));
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), user.getName()))).isEqualTo(0);
        assertThat(countRowsInTableWhere(getUsersTableReference(), format("id = %1$d AND name = '%2$s'", user.getId(), updatedName))).isEqualTo(1);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"updateDataInUserTableAsDifferentTenant"}, testName = "try to delete data from the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to delete data from the users table assigned to the different tenant than currently set")
    public void tryToDeleteDataFromUserTableAsDifferentTenant(User user, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
        ownerJdbcTemplate.execute(format("%1$s DELETE FROM  %2$s WHERE id = %3$d;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant), getUsersTableReference(), user.getId()));
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToDeleteDataFromUserTableAsDifferentTenant"}, testName = "delete data from the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to delete data from the users table assigned to the current tenant")
    public void deleteDataFromUserTableAsDifferentTenant(Object[] parameters)
    {
        User user = (User) parameters[0];
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(1);
        ownerJdbcTemplate.execute(format("%1$s DELETE FROM  %2$s WHERE id = %3$d;", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId()), getUsersTableReference(), user.getId()));
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
    }

    @Override
    @Test(dependsOnMethods = { "tryToInsertDataIntoUserTableAsDifferentTenant", "insertDataIntoUserTableAsCurrentTenant", "tryToSelectDataFromUserTableAsDifferentTenant", "tryToSelectDataFromUserTableAsSameTenant", "selectAllShouldReturnOnlyRecordsThatBelongsToCurrentTenant", "tryToUpdateDataInUserTableAsDifferentTenant", "updateDataInUserTableAsDifferentTenant", "tryToDeleteDataFromUserTableAsDifferentTenant", "deleteDataFromUserTableAsDifferentTenant" }, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }
}
