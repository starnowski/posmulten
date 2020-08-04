package com.github.starnowski.posmulten.postgresql.core.functional.tests.rls;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.functional.tests.TestNGSpringContextWithoutGenericTransactionalSupportTests;
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
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.testng.Assert.assertTrue;

public class CreateRLSForSingleTableInPublicSchemaTest extends TestNGSpringContextWithoutGenericTransactionalSupportTests {

    protected static final String USER_TENANT = "primary_tenant";
    protected static final String SECONDARY_USER_TENANT = "someXDAFAS_id";

    //TODO
    protected String getSchema() {
        return null;
    }

    protected String getUsersTableReference()
    {
        return (getSchema() == null ? "" : getSchema() + ".") + "users";
    }

    protected SetCurrentTenantIdFunctionDefinition setCurrentTenantIdFunctionDefinition;

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

    @Test(testName = "create SQL definitions", description = "Create SQL function that creates statements that set current tenant value, retrieve current tenant value and create the row level security policy for a table that is multi-tenant aware")
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

    @Test(dataProvider = "userData", dependsOnMethods = {"executeSQLDefinitions"}, testName = "try to insert data into the users table assigned to the different tenant than currently set", description = "test case assumes that row level security for users table is not going to allow to insert data into the users table assigned to the different tenant than currently set")
    public void tryToInsertPostForUserFromDifferentTenant(User user, String differentTenant)
    {
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        //TODO
//        jdbcTemplate.execute("GRANT EXECUTE ON FUNCTION " + setCurrentTenantIdFunctionDefinition.getFunctionReference() + "(text) TO \"" + CORE_OWNER_USER + "\"");
        assertThatThrownBy(() ->
                ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(differentTenant)))
        ).isInstanceOf(BadSqlGrammarException.class).getRootCause().isInstanceOf(PSQLException.class);
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
    }

    @Test(dataProvider = "userData", dependsOnMethods = {"tryToInsertPostForUserFromDifferentTenant"}, testName = "insert data into the users table assigned to the currently set", description = "test case assumes that row level security for users table is going to allow to insert data into the users table assigned to the current tenant")
    public void insertUserTestData(Object[] parameters)
    {
        User user = (User) parameters[0];
        assertThat(countRowsInTableWhere(getUsersTableReference(), "id = " + user.getId())).isEqualTo(0);
        ownerJdbcTemplate.execute(format("%5$s INSERT INTO %4$s (id, name, tenant_id) VALUES (%1$d, '%2$s', '%3$s');", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference(), setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(user.getTenantId())));
        assertTrue(isAnyRecordExists(jdbcTemplate, format("SELECT * FROM %4$s WHERE id = %1$d AND name = '%2$s' AND tenant_id = '%3$s'", user.getId(), user.getName(), user.getTenantId(), getUsersTableReference())), "The tests user should exists");
    }

    // TODO SELECT by different tenant
    // TODO SELECT by current tenant
    // TODO UPDATE by different tenant
    // TODO UPDATE by current tenant
    // TODO DELETE by different tenant
    // TODO DELETE by current tenant

    @Override
    @Test(dependsOnMethods = { "tryToInsertPostForUserFromDifferentTenant", "insertUserTestData" }, alwaysRun = true)
    public void dropAllSQLDefinitions() {
        super.dropAllSQLDefinitions();
    }
}
