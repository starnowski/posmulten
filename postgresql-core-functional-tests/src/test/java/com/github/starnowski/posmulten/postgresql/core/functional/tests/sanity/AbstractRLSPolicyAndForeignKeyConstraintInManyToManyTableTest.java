package com.github.starnowski.posmulten.postgresql.core.functional.tests.sanity;

import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.Group;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.pojos.User;
import org.postgresql.util.PSQLException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.github.starnowski.posmulten.postgresql.core.functional.tests.TestApplication.CLEAR_DATABASE_SCRIPT_PATH;
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertTrue;

public abstract class AbstractRLSPolicyAndForeignKeyConstraintInManyToManyTableTest extends FullStackTest{

    @DataProvider(name = "userData")
    protected static Object[][] userData()
    {
        return new Object[][]{
                {new User(1L, "Szymon Tarnowski", USER_TENANT)},
                {new User(2L, "John Doe", SECONDARY_USER_TENANT)}
        };
    }

    @DataProvider(name = "groupsData")
    protected static Object[][] groupsData()
    {
        return new Object[][]{
                {new Group("40e6215d-b5c6-4896-987c-f30f3678f608", "admin_tenant_1", USER_TENANT), SECONDARY_USER_TENANT},
                {new Group("3f333df6-90a4-4fda-8dd3-9485d27cee36", "admin_tenant_2", SECONDARY_USER_TENANT), USER_TENANT}
        };
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"executeSQLDefinitions"}, testName = "insert data into to user table")
    public void insertUserTestData(User user)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    @Test(dataProvider = "groupsData", dependsOnMethods = {"insertUserTestData"}, testName = "try to insert data into the groups table assigned to the different tenant than currently set", description = "test case assumes that row level security for groups table is not going to allow to insert data into the groups table assigned to the different tenant than currently set")
    public void tryToInsertDataIntoNotificationTableAsDifferentTenant(Group group, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(0);
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%1$s INSERT INTO %2$s (uuid, name, tenant_id) VALUES ('%3$s', '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(differentTenant), getGroupsTableReference(), group.getUuid(), group.getName(), group.getTenantId()))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(0);
    }

    @Test(dataProvider = "groupsData", dependsOnMethods = {"tryToInsertDataIntoNotificationTableAsDifferentTenant"}, testName = "insert data into the groups table assigned to the currently set", description = "test case assumes that row level security for groups table is going to allow to insert data into the groups table assigned to the currently set")
    public void insertDataIntoNotificationTableAsDifferentTenant(Object[] parameters)
    {
        Group group = (Group) parameters[0];
        assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%1$s INSERT INTO %2$s (uuid, name, tenant_id) VALUES ('%3$s', '%4$s', '%5$s');", setCurrentTenantIdFunctionInvocationFactory.generateStatementThatSetTenant(group.getTenantId()), getGroupsTableReference(), group.getUuid(), group.getName(), group.getTenantId()));
        assertThat(countRowsInTableWhere(getGroupsTableReference(), "uuid = '" + group.getUuid() + "'")).isEqualTo(1);
    }

    //TODO different tenant
    //TODO invalid user
    //TODO invalid group
    //TODO correct statement

    @Test(dependsOnMethods = {"insertUserTestData", "tryToInsertDataIntoNotificationTableAsDifferentTenant", "insertDataIntoNotificationTableAsDifferentTenant"}, alwaysRun = true)
    @SqlGroup({
            @Sql(value = CLEAR_DATABASE_SCRIPT_PATH,
                    config = @SqlConfig(transactionMode = ISOLATED),
                    executionPhase = BEFORE_TEST_METHOD)})
    public void deleteTestData()
    {
        assertThat(countRowsInTable(getUsersTableReference())).isEqualTo(0);
        assertThat(countRowsInTable(getGroupsTableReference())).isEqualTo(0);
    }

    @Override
    @Test(dependsOnMethods = "deleteTestData", alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }
}
