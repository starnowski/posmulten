package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducerParameters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.dropFunction
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class RLSPolicyProducerItTest extends Specification {

    def tested = new RLSPolicyProducer()

    def schema
    def table
    def policyName
    def tenantHasAuthoritiesFunction

    @Autowired
    JdbcTemplate jdbcTemplate

    def setup()
    {
        StringBuilder sb = new StringBuilder()
        sb.append("CREATE OR REPLACE FUNCTION is_tenant_starts_with_abcd(text) RETURNS BOOLEAN AS \$\$")
        sb.append("\n")
        sb.append("SELECT \$1 LIKE 'ABCD%'")
        sb.append("\n")
        sb.append("\$\$ LANGUAGE sql;")
        jdbcTemplate.execute(sb.toString())
        assertEquals(true, isFunctionExists(jdbcTemplate, "is_tenant_starts_with_abcd", null))
        def equalsCurrentTenantIdentifierFunctionInvocationFactory = { tenant ->
            "is_tenant_starts_with_abcd(" + (FunctionArgumentValueEnum.STRING.equals(tenant.getType()) ? ("'" + tenant.getValue() + "'") : tenant.getValue()) + ")"
        }
        def producer = new TenantHasAuthoritiesFunctionProducer()
        tenantHasAuthoritiesFunction = producer.produce(new TenantHasAuthoritiesFunctionProducerParameters("tenant_has_authorities_function", null, equalsCurrentTenantIdentifierFunctionInvocationFactory))
        jdbcTemplate.execute(tenantHasAuthoritiesFunction.getCreateScript())
        assertEquals(true, isFunctionExists(jdbcTemplate, "tenant_has_authorities_function", null))
    }

    def cleanup()
    {
        dropFunction(jdbcTemplate, "is_tenant_starts_with_abcd", null, "text")
        assertEquals(false, isFunctionExists(jdbcTemplate, "is_tenant_starts_with_abcd", null))
        jdbcTemplate.execute(tenantHasAuthoritiesFunction.getDropScript())
        assertEquals(false, isFunctionExists(jdbcTemplate, "tenant_has_authorities_function", null))
//        jdbcTemplate.execute(functionDefinition.getDropScript())
//        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
    }

}
