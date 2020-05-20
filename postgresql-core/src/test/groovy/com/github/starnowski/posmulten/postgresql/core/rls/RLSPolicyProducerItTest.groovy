package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducerParameters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.dropFunction
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper.mapToString
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class RLSPolicyProducerItTest extends Specification {

    public static final String IS_TENANT_ID_CORRECT_TEST_FUNCTION = "is_tenant_starts_with_abcd"
    def tested = new RLSPolicyProducer()
    def schema
    def table
    def policyName
    def policyDefinition
    def tenantHasAuthoritiesFunction

    @Autowired
    JdbcTemplate jdbcTemplate

    def setup()
    {
        StringBuilder sb = new StringBuilder()
        sb.append("CREATE OR REPLACE FUNCTION ")
        sb.append(IS_TENANT_ID_CORRECT_TEST_FUNCTION)
        sb.append("(text) RETURNS BOOLEAN AS \$\$")
        sb.append("\n")
        sb.append("SELECT \$1 LIKE 'ABCD%'")
        sb.append("\n")
        sb.append("\$\$ LANGUAGE sql;")
        jdbcTemplate.execute(sb.toString())
        assertEquals(true, isFunctionExists(jdbcTemplate, IS_TENANT_ID_CORRECT_TEST_FUNCTION, null))
        EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory = prepareEqualsCurrentTenantIdentifierFunctionInvocationFactoryForTest()
        def producer = new TenantHasAuthoritiesFunctionProducer()
        tenantHasAuthoritiesFunction = producer.produce(new TenantHasAuthoritiesFunctionProducerParameters("tenant_has_authorities_function", null, equalsCurrentTenantIdentifierFunctionInvocationFactory))
        jdbcTemplate.execute(tenantHasAuthoritiesFunction.getCreateScript())
        assertEquals(true, isFunctionExists(jdbcTemplate, "tenant_has_authorities_function", null))
        TenantHasAuthoritiesFunctionInvocationFactory dummyFactory = prepareTenantHasAuthoritiesFunctionInvocationFactoryForTestFunctionThatDetermineIfTenantIdIsCorrect()
    }

    def cleanup()
    {
        dropFunction(jdbcTemplate, IS_TENANT_ID_CORRECT_TEST_FUNCTION, null, "text")
        assertEquals(false, isFunctionExists(jdbcTemplate, IS_TENANT_ID_CORRECT_TEST_FUNCTION, null))
        jdbcTemplate.execute(tenantHasAuthoritiesFunction.getDropScript())
        assertEquals(false, isFunctionExists(jdbcTemplate, "tenant_has_authorities_function", null))
//        jdbcTemplate.execute(functionDefinition.getDropScript())
//        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
    }

    private Closure<String> prepareEqualsCurrentTenantIdentifierFunctionInvocationFactoryForTest() {
        { tenant ->
            IS_TENANT_ID_CORRECT_TEST_FUNCTION + "(" + mapToString(tenantIdValue) + ")"
        }
    }

    private Closure<String> prepareTenantHasAuthoritiesFunctionInvocationFactoryForTestFunctionThatDetermineIfTenantIdIsCorrect() {
        { tenantIdValue, permissionCommandPolicy, rlsExpressionType, table, schema ->
            IS_TENANT_ID_CORRECT_TEST_FUNCTION + "(" + mapToString(tenantIdValue) + ")"
        }
    }

}
