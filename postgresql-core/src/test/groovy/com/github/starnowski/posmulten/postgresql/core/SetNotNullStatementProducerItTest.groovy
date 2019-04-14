package com.github.starnowski.posmulten.postgresql.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static org.junit.Assert.assertEquals
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isAnyRecordExists

@SpringBootTest
class SetNotNullStatementProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new SetNotNullStatementProducer()

    String table
    String column

    @Unroll
    def "should change '#testColumn' column definition and set column as 'not null' in table '#testTable' when column value can be null before test execution" () {
        given:
            table = testTable
            column = testColumn
            assertEquals(true, isAnyRecordExists(jdbcTemplate, selectStatement(table, column, true)))

        when:
            jdbcTemplate.execute(tested.produce(testTable, testColumn))

        then:
            isAnyRecordExists(jdbcTemplate, selectStatement(table, column, false))

        where:
            testTable       |   testColumn
            "users"         |   "name"
            "users"         |   "tenant_id"
            "groups"        |   "name"
            "groups"        |   "tenant_id"
            "users_groups"  |   "tenant_id"
            "posts"         |   "tenant_id"
    }

    def cleanup() {
        jdbcTemplate.execute("ALTER TABLE " + table + " ALTER COLUMN " + column + " SET NULL;")
        assertEquals(true, isAnyRecordExists(jdbcTemplate, selectStatement(table, column, true)))
    }

    def selectStatement(String table, String column, boolean isNullAble)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT 1 FROM information_schema.columns WHERE ")
        sb.append("table_catalog = 'postgresql_core' AND ")
        sb.append("table_schema = 'public' AND ")
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
