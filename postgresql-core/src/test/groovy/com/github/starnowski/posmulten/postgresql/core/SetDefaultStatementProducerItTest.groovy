package com.github.starnowski.posmulten.postgresql.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.selectAndReturnFirstRecordAsString
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class SetDefaultStatementProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new SetDefaultStatementProducer()

    String table
    String column
    String schema

    @Unroll
    def "should change #testColumn column definition and set default value \"#defaultValue\" in table #testTable and schema '#testSchema' when column does not has any default value before test execution" () {
        given:
            table = testTable
            column = testColumn
            schema = testSchema
            assertEquals("NO_DEFAULT", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column, schema)))

        when:
            jdbcTemplate.execute((String)tested.produce(new SetDefaultStatementProducerParameters(testTable, testColumn, defaultValue, testSchema)))

        then:
            selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column, schema)) == expectedDefaultValue

        where:
            testTable       |   testColumn  |   defaultValue                                    | testSchema                ||  expectedDefaultValue
            "users"         |   "name"      |   "current_setting('poc.current_tenant')"         | null                      ||  "current_setting('poc.current_tenant'::text)"
            "users"         |   "tenant_id" |   "current_setting('poc.current_tenant')"         | null                      ||  "current_setting('poc.current_tenant'::text)"
            "groups"        |   "name"      |   "'Simon_group'"                                 | null                      ||  "'Simon_group'::character varying"
            "groups"        |   "tenant_id" |   "'tenant_one'"                                  | null                      ||  "'tenant_one'::character varying"
            "users_groups"  |   "tenant_id" |   "current_setting('posmulten_current_tenant')"   | null                      ||  "current_setting('posmulten_current_tenant'::text)"
            "posts"         |   "tenant_id" |   "'ddd'"                                         | null                      ||  "'ddd'::character varying"
            "users_groups"  |   "tenant_id" |   "current_setting('posmulten_current_tenant')"   | "public"                  ||  "current_setting('posmulten_current_tenant'::text)"
            "posts"         |   "tenant_id" |   "'ddd'"                                         | "public"                  ||  "'ddd'::character varying"
            "users_groups"  |   "tenant_id" |   "current_setting('posmulten_current_tenant')"   | "non_public_schema"       ||  "current_setting('posmulten_current_tenant'::text)"
            "posts"         |   "tenant_id" |   "'ddd'"                                         | "non_public_schema"       ||  "'ddd'::character varying"
    }

    def cleanup() {
        def tableReference = schema == null ? table : schema + "." + table
        jdbcTemplate.execute("ALTER TABLE " + tableReference + " ALTER COLUMN " + column + " DROP DEFAULT;")
        assertEquals("NO_DEFAULT", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column, schema)))
    }

    def selectStatement(String table, String column, String schema)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT COALESCE(column_default, 'NO_DEFAULT') FROM information_schema.columns WHERE ")
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
