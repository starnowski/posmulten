package com.github.starnowski.posmulten.postgresql.core.rls


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

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isAnyRecordExists
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class GetCurrentTenantIdFunctionProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new GetCurrentTenantIdFunctionProducer()

    String schema
    String functionName

    @Unroll
    def "should create function '#testFunctionName' for schema '#testSchema' (null means public) which returns type '#testReturnType' and returns correct value of property #testCurrentTenantIdProperty" () {
        given:
            functionName = testFunctionName
            schema = testSchema
            assertEquals(false, isAnyRecordExists(jdbcTemplate, selectStatement(functionName, schema)))
            def expectedStatementResult = "function_value-->" + testPropertyValue + "<--"
            def selectStatementWithStringConcat = "SELECT CONCAT('function_value-->' || " + (testSchema == null ? "" : testSchema + ".") + testFunctionName + "()" + " || '<--')"

        when:
            jdbcTemplate.execute((String)tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testReturnType)))

        then:
            isAnyRecordExists(jdbcTemplate, selectStatement(functionName, schema))

        and: "return correct result for contact statement"
            getStringResultForSelectStatement(testCurrentTenantIdProperty, testPropertyValue, selectStatementWithStringConcat) == expectedStatementResult

        where:
            testSchema              |   testFunctionName        |   testCurrentTenantIdProperty |   testReturnType  | testPropertyValue
            null                    |   "get_current_tenant"    |   "c.c_ten"                     |   null            |   "XXX-JJJ"
            "public"                |   "get_current_tenant"    |   "c.c_ten"                     |   null            |   "XXX-JJJ"
            "non_public_schema"     |   "get_current_tenant"    |   "c.c_ten"                     |   null            |   "XXX-JJJ"
    }

    def cleanup() {
        def functionReference = schema == null ? functionName : schema + "." + functionName
        jdbcTemplate.execute("DROP FUNCTION IF EXISTS " + functionReference + "()")
        assertEquals(false, isAnyRecordExists(jdbcTemplate, selectStatement(functionName, schema)))
    }

    def selectStatement(String functionName, String schema)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT 1 FROM pg_proc pg, pg_catalog.pg_namespace pgn WHERE ")
        sb.append("pg.proname = '")
        sb.append(functionName)
        sb.append("' AND ")
        if (schema == null)
        {
            sb.append("pgn.nspname = 'public'")
        } else {
            sb.append("pgn.nspname = '")
            sb.append(schema)
            sb.append("'")
        }
        sb.append(" AND ")
        sb.append("pg.pronamespace =  pgn.oid")
        sb.toString()
    }

    def getStringResultForSelectStatement(String propertyName, String propertyValue, String selectStatement)
    {
        return jdbcTemplate.execute(new StatementCallback<String>() {
            @Override
            String doInStatement(Statement statement) throws SQLException, DataAccessException {
                statement.execute("SET " + propertyName + " = '" + propertyValue + "';")
                ResultSet rs = statement.executeQuery(selectStatement);rs.next()
                return rs.getString(1)
            }
        });
    }
}
