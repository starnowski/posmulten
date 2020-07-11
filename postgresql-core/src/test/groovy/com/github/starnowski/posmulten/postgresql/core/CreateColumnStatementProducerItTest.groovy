package com.github.starnowski.posmulten.postgresql.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.isAnyRecordExists
import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.selectAndReturnFirstRecordAsString
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class CreateColumnStatementProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new CreateColumnStatementProducer()

    String table
    String column
    String schema

    @Unroll
    def "should add \"#testColumn\" column with type \"#columnType\" in table \"#testTable\" and schema \"#testSchema\" when column does not exists before test execution" () {
        given:
            table = testTable
            column = testColumn
            schema = testSchema
            assertEquals(false, isAnyRecordExists(jdbcTemplate, selectStatement(table, column, schema)))

        when:
            jdbcTemplate.execute((String)tested.produce(new CreateColumnStatementProducerParameters(testTable, testColumn, columnType, testSchema)))

        then:
            isAnyRecordExists(jdbcTemplate, selectStatement(table, column, schema))
            selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column, schema)) == expectedConcatenationOfColumnNameAndType

        where:
            testTable       |   testColumn      |   columnType                  | testSchema                    ||  expectedConcatenationOfColumnNameAndType
            "users"         |   "first_name"    |   "character varying(255)"    | null                          ||  "first_name character varying(255)"
            "users"         |   "tenant_x_id"   |   "text"                      | null                          ||  "tenant_x_id text"
            "groups"        |   "title_name"    |   "text"                      | null                          ||  "title_name text"
            "groups"        |   "tenant_x_id"   |   "character varying(45)"     | null                          ||  "tenant_x_id character varying(45)"
            "users_groups"  |   "tenant_x_id"   |   "character varying(30)"     | null                          ||  "tenant_x_id character varying(30)"
            "posts"         |   "tenant_x_id"   |   "text"                      | null                          ||  "tenant_x_id text"
            "users"         |   "first_name"    |   "character varying(255)"    | "public"                      ||  "first_name character varying(255)"
            "users"         |   "tenant_x_id"   |   "text"                      | "public"                      ||  "tenant_x_id text"
            "groups"        |   "title_name"    |   "text"                      | "public"                      ||  "title_name text"
            "groups"        |   "tenant_x_id"   |   "character varying(45)"     | "public"                      ||  "tenant_x_id character varying(45)"
            "users_groups"  |   "tenant_x_id"   |   "character varying(30)"     | "public"                      ||  "tenant_x_id character varying(30)"
            "posts"         |   "tenant_x_id"   |   "text"                      | "public"                      ||  "tenant_x_id text"
            "users"         |   "first_name"    |   "character varying(255)"    | "non_public_schema"           ||  "first_name character varying(255)"
            "users"         |   "tenant_x_id"   |   "text"                      | "non_public_schema"           ||  "tenant_x_id text"
            "groups"        |   "title_name"    |   "text"                      | "non_public_schema"           ||  "title_name text"
            "groups"        |   "tenant_x_id"   |   "character varying(45)"     | "non_public_schema"           ||  "tenant_x_id character varying(45)"
            "users_groups"  |   "tenant_x_id"   |   "character varying(30)"     | "non_public_schema"           ||  "tenant_x_id character varying(30)"
            "posts"         |   "tenant_x_id"   |   "text"                      | "non_public_schema"           ||  "tenant_x_id text"
    }

    def cleanup() {
        def tableReference = schema == null ? table : schema + "." + table
        jdbcTemplate.execute("ALTER TABLE " + tableReference + " DROP COLUMN " + column + ";")
        assertEquals(false, isAnyRecordExists(jdbcTemplate, selectStatement(table, column, schema)))
    }

    def selectStatement(String table, String column, String schema)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT CONCAT(column_name || ' ' || data_type , CASE character_maximum_length WHEN NULL THEN '' ELSE '(' || character_maximum_length || ')' END ) FROM information_schema.columns WHERE ")
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
        sb.append("'")
        sb.toString()
    }
}
