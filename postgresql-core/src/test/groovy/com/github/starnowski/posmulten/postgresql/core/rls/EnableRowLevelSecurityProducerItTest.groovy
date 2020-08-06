package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.selectAndReturnFirstRecordAsString
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class EnableRowLevelSecurityProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new EnableRowLevelSecurityProducer()

    String table
    String schema
    SQLDefinition sqlDefinition

    @Unroll
    def "should enable row level security for table #testTable and schema #testSchema" () {
        given:
            table = testTable
            schema = testSchema
            assertEquals("Table " + testTable + " has enabled row level security", "f", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, schema)))

        when:
            sqlDefinition = tested.produce(testTable, testSchema)
            jdbcTemplate.execute(sqlDefinition.getCreateScript())

        then:
            assertEquals("Table " + testTable + " does not have enabled row level security", "t", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, schema)))

        where:
            testTable           |   testSchema
            "users"             |   null
            "posts"             |   null
            "users_groups"      |   null
            "users"             |   "public"
            "posts"             |   "public"
            "users_groups"      |   "public"
            "users"             |   "non_public_schema"
            "posts"             |   "non_public_schema"
            "users_groups"      |   "non_public_schema"
    }

    def cleanup() {
        jdbcTemplate.execute(sqlDefinition.getDropScript())
        assertEquals("Table " + table + " still has enabled row level security", "f", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, schema)))
    }

    def selectStatement(String table, String schema)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT pc.relrowsecurity FROM pg_class pc, pg_catalog.pg_namespace pg ")
        sb.append("WHERE")
        sb.append(" pc.relname = '")
        sb.append(table)
        sb.append("' AND pc.relnamespace = pg.oid AND pg.nspname = '")
        if (schema == null)
            sb.append("public")
        else
            sb.append(schema)
        sb.append("';")
        sb.toString()
    }
}
