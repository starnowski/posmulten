package com.github.starnowski.posmulten.postgresql.core

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static org.junit.Assert.assertEquals
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists

@SpringBootTest(classes = [TestApplication.class])
class SetNotNullStatementProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new SetNotNullStatementProducer()

    String table
    String column
    String schema
    SQLDefinition sqlDefinition

    @Unroll
    def "should change #testColumn column definition and set column as 'not null' in table #testTable and schema #testSchema when column value can be null before test execution" () {
        given:
            table = testTable
            column = testColumn
            schema = testSchema
            assertEquals(true, isAnyRecordExists(jdbcTemplate, selectStatement(table, column, schema, true)))

        when:
            sqlDefinition = tested.produce(new SetNotNullStatementProducerParameters(testTable, testColumn, testSchema))
            jdbcTemplate.execute(sqlDefinition.getCreateScript())

        then:
            isAnyRecordExists(jdbcTemplate, selectStatement(table, column, schema, false))

        where:
            testTable       |   testColumn  |   testSchema
            "users"         |   "name"      |   null
            "users"         |   "name"      |   "public"
            "users"         |   "name"      |   "non_public_schema"
            "users"         |   "tenant_id" |   null
            "users"         |   "tenant_id" |   "public"
            "users"         |   "tenant_id" |   "non_public_schema"
            "groups"        |   "name"      |   null
            "groups"        |   "name"      |   "public"
            "groups"        |   "name"      |   "non_public_schema"
            "groups"        |   "tenant_id" |   null
            "groups"        |   "tenant_id" |   "public"
            "groups"        |   "tenant_id" |   "non_public_schema"
            "users_groups"  |   "tenant_id" |   null
            "users_groups"  |   "tenant_id" |   "public"
            "users_groups"  |   "tenant_id" |   "non_public_schema"
            "posts"         |   "tenant_id" |   null
            "posts"         |   "tenant_id" |   "public"
            "posts"         |   "tenant_id" |   "non_public_schema"
    }

    def cleanup() {
        jdbcTemplate.execute(sqlDefinition.getDropScript())
        assertEquals(true, isAnyRecordExists(jdbcTemplate, selectStatement(table, column, schema, true)))
    }

    def selectStatement(String table, String column, String schema, boolean isNullAble)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT 1 FROM information_schema.columns WHERE ")
        sb.append("table_catalog = 'postgresql_core' AND ")
        if (schema == null)
        {
            sb.append("table_schema = 'public'")
        } else {
            sb.append("table_schema = '")
            sb.append(schema)
            sb.append("'")
        }
        sb.append(" AND ")
        sb.append("table_name = '")
        sb.append(table)
        sb.append("' AND ")
        sb.append("column_name = '")
        sb.append(column)
        sb.append("' AND ")
        sb.append("is_nullable = '")
        sb.append(isNullAble ? "YES" : "NO")
        sb.append("'")
        sb.toString()
    }
}
