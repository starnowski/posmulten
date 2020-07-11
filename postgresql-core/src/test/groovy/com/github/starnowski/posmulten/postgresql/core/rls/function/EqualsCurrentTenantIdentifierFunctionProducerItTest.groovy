package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.TestApplication
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

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.dropFunction
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isFunctionExists
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class EqualsCurrentTenantIdentifierFunctionProducerItTest extends Specification {

    private static String VALID_CURRENT_TENANT_ID_PROPERTY_NAME = "c.c_ten"

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new EqualsCurrentTenantIdentifierFunctionProducer()
    def getCurrentTenantIdFunctionProducer = new GetCurrentTenantIdFunctionProducer()
    def setCurrentTenantIdFunctionProducer = new SetCurrentTenantIdFunctionProducer()

    String schema
    String functionName
    String argumentType
    GetCurrentTenantIdFunctionDefinition getCurrentTenantIdFunctionDefinition
    SetCurrentTenantIdFunctionDefinition setCurrentTenantIdFunctionDefinition
    EqualsCurrentTenantIdentifierFunctionDefinition functionDefinition

    @Unroll
    def "should create function '#testFunctionName' for schema '#testSchema' (null means public) which returns type '#testReturnType' and returns correct value of property #testCurrentTenantIdProperty" () {
        given:
            functionName = testFunctionName
            schema = testSchema
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
            getCurrentTenantIdFunctionDefinition = getCurrentTenantIdFunctionProducer.produce(new GetCurrentTenantIdFunctionProducerParameters("get_current_tenant", testCurrentTenantIdProperty, testSchema, null))
            setCurrentTenantIdFunctionDefinition = setCurrentTenantIdFunctionProducer.produce(new SetCurrentTenantIdFunctionProducerParameters("set_current_tenant", testCurrentTenantIdProperty, testSchema, null))
            jdbcTemplate.execute(getCurrentTenantIdFunctionDefinition.getCreateScript())
            jdbcTemplate.execute(setCurrentTenantIdFunctionDefinition.getCreateScript())

        when:
            functionDefinition = tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, null, getCurrentTenantIdFunctionDefinition))
            jdbcTemplate.execute(functionDefinition.getCreateScript())

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        and: "return correct result"
            returnSelectStatementResultAfterSettingCurrentTenantId(testCurrentTenantIdValue, passedValue) == exptectedResult

        where:
            testSchema              |   testFunctionName            |   testCurrentTenantIdProperty             |  testCurrentTenantIdValue     |   passedValue             || exptectedResult
            null                    |   "is_current_tenant"         |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "ASDFZXCVZS"            ||   true
            "public"                |   "is_current_tenant"         |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "ASDFZXCVZS"            ||   true
            "non_public_schema"     |   "is_current_tenant"         |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "ASDFZXCVZS"            ||   true
            null                    |   "is_current_tenant"         |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "1234DADF"              ||   false
            "public"                |   "is_current_tenant"         |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "VZXCV"                 ||   false
            "non_public_schema"     |   "is_current_tenant"         |   VALID_CURRENT_TENANT_ID_PROPERTY_NAME   |   "ASDFZXCVZS"                |   "FDFGSFGS"              ||   false
    }

    def cleanup() {
        def argumentTypePhrase = argumentType == null ? "VARCHAR(255)" : argumentType
        dropFunction(jdbcTemplate, functionName, schema, argumentTypePhrase)
        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
        jdbcTemplate.execute(getCurrentTenantIdFunctionDefinition.getDropScript())
        jdbcTemplate.execute(setCurrentTenantIdFunctionDefinition.getDropScript())
    }

    def returnSelectStatementResultAfterSettingCurrentTenantId(String propertyValue, String passedValue)
    {
        return jdbcTemplate.execute(new StatementCallback<Boolean>() {
            @Override
            Boolean doInStatement(Statement statement) throws SQLException, DataAccessException {
                statement.execute(setCurrentTenantIdFunctionDefinition.generateStatementThatSetTenant(propertyValue))
                def selectStatement = String.format("SELECT %s;", functionDefinition.returnEqualsCurrentTenantIdentifierFunctionInvocation(forString(passedValue)))
                ResultSet rs = statement.executeQuery(selectStatement)
                rs.next()
                return rs.getBoolean(1)
            }
        })
    }
}
