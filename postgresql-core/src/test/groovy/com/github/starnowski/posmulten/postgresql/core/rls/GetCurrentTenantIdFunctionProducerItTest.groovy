package com.github.starnowski.posmulten.postgresql.core.rls


import com.github.starnowski.posmulten.postgresql.core.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

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

        when:
            jdbcTemplate.execute((String)tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testReturnType)))

        then:
            isAnyRecordExists(jdbcTemplate, selectStatement(functionName, schema))

        where:
            testSchema              |   testFunctionName        |   testCurrentTenantIdProperty |   testReturnType
            null                    |   "get_current_tenant"    |   "c_ten"                     |   null
            "public"                |   "get_current_tenant"    |   "c_ten"                     |   null
            "non_public_schema"     |   "get_current_tenant"    |   "c_ten"                     |   null
    }

    def cleanup() {
        def functionReference = schema == null ? functionName : schema + "." + functionName
        jdbcTemplate.execute("DROP FUNCTION IF EXISTS " + functionReference + "()")
        assertEquals(false, isAnyRecordExists(jdbcTemplate, selectStatement(functionName, schema)))
    }

    def selectStatement(String functionName, String schema)
    {
        //SELECT pg.*, pgn.* FROM pg_proc pg, pg_catalog.pg_namespace pgn WHERE pg.proname = 'is_valid_tenant' AND pg.pronamespace =  pgn.oid AND pgn.nspname = 'public';
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
}
