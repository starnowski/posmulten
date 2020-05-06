package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.TestUtils
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
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class SetCurrentTenantIdFunctionProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new SetCurrentTenantIdFunctionProducer()

    String schema
    String functionName
    String argumentType

    @Unroll
    def "should create function '#testFunctionName' for schema '#testSchema' (null means public) which expect argument of type '#testArgumentType' (null means 'text') and set correct value of property #testCurrentTenantIdProperty" () {
        given:
            functionName = testFunctionName
            schema = testSchema
            argumentType = testArgumentType
            assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
            def expectedStatementResult = "function_value-->" + testPropertyValue + "<--"
            def selectStatementWithStringConcat = returnSelectStatementWithStringConcat(testCurrentTenantIdProperty)

        when:
            jdbcTemplate.execute((String)tested.produce(new SetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testArgumentType)))

        then:
            isFunctionExists(jdbcTemplate, functionName, schema)

        and: "return correct result for contact statement"
            getStringResultForSelectStatement(TestUtils.returnFunctionReference(testFunctionName, testSchema), testPropertyValue, selectStatementWithStringConcat) == expectedStatementResult

        where:
            testSchema              |   testFunctionName            |   testCurrentTenantIdProperty     |   testArgumentType    | testPropertyValue
            null                    |   "set_current_tenant"        |   "c.c_ten"                       |   null                |   "XXX-JJJ"
            "public"                |   "set_current_tenant"        |   "c.c_ten"                       |   null                |   "XXX-JJJ"
            "non_public_schema"     |   "set_current_tenant"        |   "c.c_ten"                       |   null                |   "XXX-JJJ"
            null                    |   "set_current_tenant"        |   "c.c_ten"                       |   "text"              |   "dfafdzcxvzcxv"
            "public"                |   "set_current_tenant"        |   "c.c_ten"                       |   "text"              |   "jjjjhhkkl"
            "non_public_schema"     |   "set_current_tenant"        |   "c.c_ten"                       |   "text"              |   "this_is_testtenantsssdd"
            null                    |   "tenant_val"                |   "con.tenant_id"                 |   "VARCHAR(128)"      |   "dfafdzcxvzcxv"
            "public"                |   "give_me_tenant"            |   "pos.tenant"                    |   "VARCHAR(32)"       |   "abc"
            "non_public_schema"     |   "setup_current_tenant"      |   "t.id"                          |   "text"              |   "my_old_tenant"
    }

    def cleanup() {
        def argumentTypePhrase = argumentType == null ? "text" : argumentType
        dropFunction(jdbcTemplate, functionName, schema, argumentTypePhrase)
        assertEquals(false, isFunctionExists(jdbcTemplate, functionName, schema))
    }

    def getStringResultForSelectStatement(String functionReference, String propertyValue, String selectStatement)
    {
        return jdbcTemplate.execute(new StatementCallback<String>() {
            @Override
            String doInStatement(Statement statement) throws SQLException, DataAccessException {
                statement.execute("SELECT " + functionReference + "('" + propertyValue + "')")
                ResultSet rs = statement.executeQuery(selectStatement)
                rs.next()
                return rs.getString(1)
            }
        })
    }

    private String returnSelectStatementWithStringConcat(String propertyName) {
        "SELECT CONCAT('function_value-->' || " + "current_setting('" + propertyName + "')" + " || '<--')"
    }
}
