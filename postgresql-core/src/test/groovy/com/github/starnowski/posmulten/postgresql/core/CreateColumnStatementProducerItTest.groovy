package com.github.starnowski.posmulten.postgresql.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.isAnyRecordExists
import static com.github.starnowski.posmulten.postgresql.core.TestUtils.selectAndReturnFirstRecordAsString
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class CreateColumnStatementProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new CreateColumnStatementProducer()

    String table
    String column

    @Unroll
    def "should add \"#testColumn\" column with type \"#columnType\" in table \"#testTable\" when column does not exists before test execution" () {
        given:
            table = testTable
            column = testColumn
            assertEquals(false, isAnyRecordExists(jdbcTemplate, selectStatement(table, column)))

        when:
            jdbcTemplate.execute((String)tested.produce(testTable, testColumn, columnType))

        then:
            isAnyRecordExists(jdbcTemplate, selectStatement(table, column))
            selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column)) == expectedDefaultValue

        where:
            testTable       |   testColumn      |   columnType                  ||  expectedDefaultValue
            "users"         |   "first_name"    |   "character varying(255)"    ||  "character varying(255)"
            "users"         |   "tenant_x_id"   |   "text"                      ||  "text"
            "groups"        |   "title_name"    |   "text"                      ||  "text"
            "groups"        |   "tenant_x_id"   |   "character varying(45)"     ||  "character varying(45)"
            "users_groups"  |   "tenant_x_id"   |   "character varying(30)"     ||  "character varying(30)"
            "posts"         |   "tenant_x_id"   |   "text"                      ||  "text"
    }

    def cleanup() {
        jdbcTemplate.execute("ALTER TABLE " + table + " DROP COLUMN " + column + ";")
        assertEquals(false, isAnyRecordExists(jdbcTemplate, selectStatement(table, column)))
    }

    def selectStatement(String table, String column)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT CONCAT(column_name || ' ' || data_type , CASE character_maximum_length WHEN NULL THEN '' ELSE '(' || character_maximum_length || ')' END ) FROM information_schema.columns WHERE ")
        sb.append("table_catalog = 'postgresql_core' AND ")
        sb.append("table_schema = 'public' AND ")
        sb.append("table_name = '")
        sb.append(table)
        sb.append("' AND ")
        sb.append("column_name = '")
        sb.append(column)
        sb.append("'")
        sb.toString()
    }
}
