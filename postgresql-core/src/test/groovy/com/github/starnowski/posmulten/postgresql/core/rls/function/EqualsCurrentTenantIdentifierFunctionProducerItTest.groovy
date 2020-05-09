package com.github.starnowski.posmulten.postgresql.core.rls.function

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

    private static String VALID_CURRENT_TENANT_ID_PROPERTY_NAME = "c.c_ten"

    @Autowired
    JdbcTemplate jdbcTemplate

    static Logger logger = Logger.getLogger(GetCurrentTenantIdFunctionProducerItTest.class.getName())

    def tested = new EqualsCurrentTenantIdentifierFunctionProducer()
    def getCurrentTenantIdFunctionProducer = new GetCurrentTenantIdFunctionProducer()
    def setCurrentTenantIdFunctionProducer = new SetCurrentTenantIdFunctionProducer()

    String schema
    String functionName
    String argumentType
    GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition
    SetCurrentTenantIdFunctionDefinition setCurrentTenantIdFunctionDefinition

    @Unroll
    def "should create function '#testFunctionName' for schema '#testSchema' (null means public) which returns type '#testReturnType' and returns correct value of property #testCurrentTenantIdProperty" () {
        given:
            functionName = testFunctionName
            schema = testSchema
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
            getCurrentTenantIdFunctionDefinition = getCurrentTenantIdFunctionProducer.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, null))
            setCurrentTenantIdFunctionDefinition = setCurrentTenantIdFunctionProducer.produce(new SetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, null))
            jdbcTemplate.execute(getCurrentTenantIdFunctionDefinition.getCreateScript())
            jdbcTemplate.execute(setCurrentTenantIdFunctionDefinition.getCreateScript())

        when:
            def definition = tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, null, getCurrentTenantIdFunctionDefinition))
            jdbcTemplate.execute(definition.getCreateScript())

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        and: "return correct result for contact statement"
            getStringResultForSelectStatement(expectedCurrentTenantId, passedTenantId) == exptectedResult

        where:
            testSchema              |   testFunctionName            |   testCurrentTenantIdProperty             |  testCurrentTenantIdProperty  |   passedValue             || exptectedResult
            null                    |   "get_current_tenant"        |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "ASDFZXCVZS"            ||   "t"
            "public"                |   "get_current_tenant"        |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "ASDFZXCVZS"            ||   "t"
            "non_public_schema"     |   "get_current_tenant"        |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "ASDFZXCVZS"            ||   "t"
            null                    |   "get_current_tenant"        |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "1234DADF"              ||   "f"
            "public"                |   "get_current_tenant"        |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "VZXCV"                 ||   "f"
            "non_public_schema"     |   "get_current_tenant"        |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "FDFGSFGS"              ||   "f"
    }

    def cleanup() {
        def argumentTypePhrase = argumentType == null ? "text" : argumentType
        dropFunction(jdbcTemplate, functionName, schema, argumentTypePhrase)
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
