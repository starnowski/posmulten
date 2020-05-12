package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.StatementCallback
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.dropFunction
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.ALL
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.USING
import static java.lang.String.format
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class TenantHasAuthoritiesFunctionProducerItTest extends Specification {

    def tested = new TenantHasAuthoritiesFunctionProducer()

    EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory
    def functionName
    def schema
    def functionDefinition

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
    def "should create function '#testFunctionName' for schema '#testSchema' (null means public) which returns expected result (#exptectedResult) for passed tenant value (#passedValue)" () {
        given:
            functionName = testFunctionName
            schema = testSchema
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))

        when:
            functionDefinition = tested.produce(new TenantHasAuthoritiesFunctionProducerParameters(testFunctionName, testSchema, equalsCurrentTenantIdentifierFunctionInvocationFactory))
            jdbcTemplate.execute(functionDefinition.getCreateScript())

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        and: "return correct result"
            returnSelectStatementResultAfterSettingCurrentTenantId(passedValue) == exptectedResult

        where:
            testSchema              |   testFunctionName                    |   passedValue                 || exptectedResult
            null                    |   "tenant_has_authorities_function"   |   "ABCDE"                     ||   true
            "public"                |   "tenant_has_authorities_function"   |   "ABCDE"                     ||   true
            "non_public_schema"     |   "tenant_has_authorities_function"   |   "ABCDE"                     ||   true
            null                    |   "tenant_has_privliges"              |   "ABCDE"                     ||   true
            "public"                |   "tenant_has_privliges"              |   "ABCDE"                     ||   true
            "non_public_schema"     |   "tenant_has_privliges"              |   "ABCDE"                     ||   true
            null                    |   "tenant_has_authorities_function"   |   "ABEEE"                     ||   false
            "public"                |   "tenant_has_authorities_function"   |   "ABEEE"                     ||   false
            "non_public_schema"     |   "tenant_has_authorities_function"   |   "ABEEE"                     ||   false
            null                    |   "tenant_has_privliges"              |   "ABEEE"                     ||   false
            "public"                |   "tenant_has_privliges"              |   "ABEEE"                     ||   false
            "non_public_schema"     |   "tenant_has_privliges"              |   "ABEEE"                     ||   false
    }

    def returnSelectStatementResultAfterSettingCurrentTenantId(String passedValue)
    {
        return jdbcTemplate.execute(new StatementCallback<Boolean>() {
            @Override
            Boolean doInStatement(Statement statement) throws SQLException, DataAccessException {
                def selectStatement = format("SELECT %s;", functionDefinition.returnTenantHasAuthoritiesFunctionInvocation(forString(passedValue), ALL, USING, null, null))
                System.out.println(selectStatement)
                ResultSet rs = statement.executeQuery(selectStatement)
                rs.next()
                return rs.getBoolean(1)
            }
        })
    }

    def cleanup()
    {
        dropFunction(jdbcTemplate, "is_tenant_starts_with_abcd", null, "text")
        assertEquals(false, isFunctionExists(jdbcTemplate, "is_tenant_starts_with_abcd", null))
        jdbcTemplate.execute(functionDefinition.getDropScript())
        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
    }
}
