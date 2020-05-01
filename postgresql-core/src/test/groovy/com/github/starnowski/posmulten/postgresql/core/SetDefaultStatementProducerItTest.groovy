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

    @Unroll
    def "should change #testColumn column definition and set default value \"#defaultValue\" in table #testTable when column does not has any default value before test execution" () {
        given:
            table = testTable
            column = testColumn
            assertEquals("NO_DEFAULT", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column)))

        when:
            jdbcTemplate.execute((String)tested.produce(new SetDefaultStatementProducerParameters(testTable, testColumn, defaultValue, null)))

        then:
            selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column)) == expectedDefaultValue

        where:
            testTable       |   testColumn  |   defaultValue                                    ||  expectedDefaultValue
            "users"         |   "name"      |   "current_setting('poc.current_tenant')"         ||  "current_setting('poc.current_tenant'::text)"
            "users"         |   "tenant_id" |   "current_setting('poc.current_tenant')"         ||  "current_setting('poc.current_tenant'::text)"
            "groups"        |   "name"      |   "'Simon_group'"                                 ||  "'Simon_group'::character varying"
            "groups"        |   "tenant_id" |   "'tenant_one'"                                  ||  "'tenant_one'::character varying"
            "users_groups"  |   "tenant_id" |   "current_setting('posmulten_current_tenant')"   ||  "current_setting('posmulten_current_tenant'::text)"
            "posts"         |   "tenant_id" |   "'ddd'"                                         ||  "'ddd'::character varying"
    }

    def cleanup() {
        jdbcTemplate.execute("ALTER TABLE " + table + " ALTER COLUMN " + column + " DROP DEFAULT;")
        assertEquals("NO_DEFAULT", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column)))
    }

    def selectStatement(String table, String column)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT COALESCE(column_default, 'NO_DEFAULT') FROM information_schema.columns WHERE ")
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
