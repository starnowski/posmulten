package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import java.util.logging.Logger

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.dropFunction
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class EqualsCurrentTenantIdentifierFunctionProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    static Logger logger = Logger.getLogger(GetCurrentTenantIdFunctionProducerItTest.class.getName())

    def tested = new EqualsCurrentTenantIdentifierFunctionProducer()

    String schema
    String functionName

    @Unroll
    def "should create function '#testFunctionName' for schema '#testSchema' (null means public) which returns type '#testReturnType' and returns correct value of property #testCurrentTenantIdProperty" () {
        given:
            functionName = testFunctionName
            schema = testSchema
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))

        when:
            jdbcTemplate.execute((String)tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, null, "")))

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        and: "return correct result for contact statement"
            getStringResultForSelectStatement(expectedCurrentTenantId, passedTenantId) == exptectedResult

        where:
            testSchema              |   testFunctionName            |   expectedCurrentTenantId         |  passedTenantId           | exptectedResult
            null                    |   "get_current_tenant"        |   "ASDFZXCVZS"                    |   "ASDFZXCVZS"            |   "t"
            "public"                |   "get_current_tenant"        |   "ASDFZXCVZS"                    |   "ASDFZXCVZS"            |   "t"
            "non_public_schema"     |   "get_current_tenant"        |   "ASDFZXCVZS"                    |   "ASDFZXCVZS"            |   "t"
    }

    def cleanup() {
        dropFunction(jdbcTemplate, functionName, schema)
        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
    }

    def getStringResultForSelectStatement(String propertyName, String propertyValue)
    {
//        return jdbcTemplate.execute(new StatementCallback<String>() {
//            @Override
//            String doInStatement(Statement statement) throws SQLException, DataAccessException {
//                statement.execute("SET " + propertyName + " = '" + propertyValue + "';")
//                ResultSet rs = statement.executeQuery(selectStatement)
//                rs.next()
//                return rs.getString(1)
//            }
//        })
        //TODO
        null
    }
}
