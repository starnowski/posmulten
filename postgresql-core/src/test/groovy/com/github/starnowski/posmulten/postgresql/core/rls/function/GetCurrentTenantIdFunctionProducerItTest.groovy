package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.RandomString
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
import java.util.logging.Logger

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.dropFunction
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isFunctionExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class GetCurrentTenantIdFunctionProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    static Logger logger = Logger.getLogger(GetCurrentTenantIdFunctionProducerItTest.class.getName())

    def tested = new GetCurrentTenantIdFunctionProducer()

    String schema
    String functionName

    @Unroll
    def "should create function '#testFunctionName' for schema '#testSchema' (null means public) which returns type '#testReturnType' and returns correct value of property #testCurrentTenantIdProperty" () {
        given:
            functionName = testFunctionName
            schema = testSchema
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
            def expectedStatementResult = "function_value-->" + testPropertyValue + "<--"
            def selectStatementWithStringConcat = returnSelectStatementWithStringConcat(testSchema, testFunctionName)

        when:
            jdbcTemplate.execute(tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testReturnType)).getCreateScript())

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        and: "return correct result for contact statement"
            getStringResultForSelectStatement(testCurrentTenantIdProperty, testPropertyValue, selectStatementWithStringConcat) == expectedStatementResult

        where:
            testSchema              |   testFunctionName            |   testCurrentTenantIdProperty     |   testReturnType  | testPropertyValue
            null                    |   "get_current_tenant"        |   "c.c_ten"                       |   null            |   "XXX-JJJ"
            "public"                |   "get_current_tenant"        |   "c.c_ten"                       |   null            |   "XXX-JJJ"
            "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   null            |   "XXX-JJJ"
            null                    |   "get_current_tenant"        |   "c.c_ten"                       |   "text"          |   "dfafdzcxvzcxv"
            "public"                |   "get_current_tenant"        |   "c.c_ten"                       |   "text"          |   "jjjjhhkkl"
            "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   "text"          |   "this_is_testtenantsssdd"
            null                    |   "cur_tenant_val"            |   "con.tenant_id"                 |   "VARCHAR(128)"  |   "dfafdzcxvzcxv"
            "public"                |   "give_me_tenant"            |   "pos.tenant"                    |   "VARCHAR(32)"   |   "abc"
            "non_public_schema"     |   "return_current_tenant"     |   "t.id"                          |   "text"          |   "my_old_tenant"
    }

    @Unroll
    def "should create a function for the random function name, schema '#testSchema', property name and return the value that matches the random generated value" () {
        given:
            def r = new RandomString(12, new Random(), RandomString.lower)
            schema = testSchema
            functionName = r.nextString()
            def currentTenantIdProperty = r.nextString() + "." + r.nextString()
            def propertyValue = r.nextString()
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
            def expectedStatementResult = "function_value-->" + propertyValue + "<--"
            def selectStatementWithStringConcat = returnSelectStatementWithStringConcat(testSchema, functionName)
            logger.log(java.util.logging.Level.INFO, "Random function name: " + functionName)
            logger.log(java.util.logging.Level.INFO, "Random current tenant property name: " + currentTenantIdProperty)
            logger.log(java.util.logging.Level.INFO, "Random tenant property value: " + propertyValue)

        when:
            jdbcTemplate.execute(tested.produce(new GetCurrentTenantIdFunctionProducerParameters(functionName, currentTenantIdProperty, testSchema, null)).getCreateScript())

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        and: "return correct result for contact statement"
            getStringResultForSelectStatement(currentTenantIdProperty, propertyValue, selectStatementWithStringConcat) == expectedStatementResult

        where:
            testSchema << [null, "public", "non_public_schema"]
    }

    def cleanup() {
        dropFunction(jdbcTemplate, functionName, schema)
        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
    }

    def getStringResultForSelectStatement(String propertyName, String propertyValue, String selectStatement)
    {
        return jdbcTemplate.execute(new StatementCallback<String>() {
            @Override
            String doInStatement(Statement statement) throws SQLException, DataAccessException {
                statement.execute("SET " + propertyName + " = '" + propertyValue + "';")
                ResultSet rs = statement.executeQuery(selectStatement)
                rs.next()
                return rs.getString(1)
            }
        })
    }

    private String returnSelectStatementWithStringConcat(String testSchema, String testFunctionName) {
        "SELECT CONCAT('function_value-->' || " + (testSchema == null ? "" : testSchema + ".") + testFunctionName + "()" + " || '<--')"
    }
}
