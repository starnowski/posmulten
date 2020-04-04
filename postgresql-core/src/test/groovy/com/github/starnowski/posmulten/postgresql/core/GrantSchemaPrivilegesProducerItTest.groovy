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
class GrantSchemaPrivilegesProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new GrantSchemaPrivilegesProducer()

    String user
    String schema
    String[] privileges

    @Unroll
    def "should add \"#testColumn\" column with type \"#columnType\" in table \"#testTable\" when column does not exists before test execution" () {
        given:
            user = testUser
            schema = testSchema
            privileges = testPrivileges
            for (String privilege : privileges) {
                assertEquals("false", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege)))
            }

        when:
            jdbcTemplate.execute((String)tested.produce(schema, user, privileges))

        then:
        isAnyRecordExists(jdbcTemplate, selectStatement(table, column))
        selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(table, column)) == expectedConcatenationOfColumnNameAndType

        where:
        testTable       |   testColumn      |   columnType                  ||  expectedConcatenationOfColumnNameAndType
        "users"         |   "first_name"    |   "character varying(255)"    ||  "first_name character varying(255)"
        "users"         |   "tenant_x_id"   |   "text"                      ||  "tenant_x_id text"
        "groups"        |   "title_name"    |   "text"                      ||  "title_name text"
        "groups"        |   "tenant_x_id"   |   "character varying(45)"     ||  "tenant_x_id character varying(45)"
        "users_groups"  |   "tenant_x_id"   |   "character varying(30)"     ||  "tenant_x_id character varying(30)"
        "posts"         |   "tenant_x_id"   |   "text"                      ||  "tenant_x_id text"
    }

    def cleanup() {
        for (String privilege : privileges) {
            jdbcTemplate.execute("REVOKE " + privilege + " ON SCHEMA " + schema + " FROM  \"" + user + "\";")
            assertEquals("false", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege)))
        }
    }

    def selectStatement(String user, String schema, String privilege)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT pg_catalog.has_schema_privilege(' ")
        sb.append(user)
        sb.append("', '")
        sb.append(schema)
        sb.append("', '")
        sb.append(privilege)
        sb.append("') AS USER_HAS_PRIVILEGE;")
        sb.toString()
    }
}
