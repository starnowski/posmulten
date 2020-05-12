package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.dropFunction
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class TenantHasAuthoritiesFunctionProducerItTest extends Specification {

    def tested = new TenantHasAuthoritiesFunctionProducer()

    EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory

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
        equalsCurrentTenantIdentifierFunctionInvocationFactory = { tenant ->
            "is_tenant_starts_with_abcd(" + (FunctionArgumentValueEnum.STRING.equals(tenant.getType()) ? ("'" + tenant.getValue() + "'") : tenant.getValue()) + ")"
        }
    }

    @Unroll
    def "should create sql statement" ()
    {
        given:
            def result = true

        expect:
            result == true
        //TODO
    }

    def cleanup()
    {
        dropFunction(jdbcTemplate, "is_tenant_starts_with_abcd", null, "text")
        assertEquals(false, isFunctionExists(jdbcTemplate, "is_tenant_starts_with_abcd", null))
    }
}
