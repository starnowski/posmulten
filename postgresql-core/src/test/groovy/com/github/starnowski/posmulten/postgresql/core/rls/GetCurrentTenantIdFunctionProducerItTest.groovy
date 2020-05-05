package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.SetNotNullStatementProducerParameters
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
    String currentTenantIdProperty
    String returnType

    @Unroll
    def "should create function '#testFunctionName' for schema '#testSchema' (null means public) which returns type '#testReturnType' and returns correct value of property #testCurrentTenantIdProperty" () {
        given:
            table = testTable
            column = testColumn
            schema = testSchema
            assertEquals(true, isAnyRecordExists(jdbcTemplate, selectStatement(table, column, schema, true)))

        when:
            jdbcTemplate.execute((String)tested.produce(new SetNotNullStatementProducerParameters(testTable, testColumn, testSchema)))

        then:
            isAnyRecordExists(jdbcTemplate, selectStatement(table, column, schema, false))

        where:
            testSchema              |   testFunctionName        |   testCurrentTenantIdProperty |   testReturnType
            null                    |   "get_current_tenant"    |   "c_ten"                     |   null
            "public"                |   "get_current_tenant"    |   "c_ten"                     |   null
            "non_public_schema"     |   "get_current_tenant"    |   "c_ten"                     |   null
    }
}
