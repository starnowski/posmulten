package com.github.starnowski.posmulten.postgresql.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.TestUtils.selectAndReturnFirstRecordAsString
import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class GrantTablePrivilegesProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate

    def tested = new GrantTablePrivilegesProducer()

    String user
    String table
    String schema
    String[] privileges
    String[] expectedPrivileges

    @Unroll
    def "should add \"#testPrivileges\" privileges to \"#testUser\" user for \"#testTable\" table, \"#testSchema\" schema" () {
        given:
            user = testUser
            schema = testSchema
            privileges = testPrivileges
            expectedPrivileges = testExpectedPrivileges
            for (String privilege : testExpectedPrivileges) {
                assertEquals("User " + testUser + " has privilege " + privilege, "f", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege)))
            }

        when:
            jdbcTemplate.execute(tested.produce(testSchema, testUser, testPrivileges))

        then:
            for (String privilege : testExpectedPrivileges) {
                assertEquals("User " + testUser + " does not have privilege " + privilege, "t", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege)))
            }

        where:
            testUser                        |   testSchema                  |   testTable   |   testPrivileges          ||  testExpectedPrivileges
            "postgresql-core-user"          |   "non_public_schema"         |   "users"     |   ["INSERT"]              ||  ["INSERT"]
            "postgresql-core-user"          |   "non_public_schema"         |   "users"     |   ["SELECT"]              ||  ["SELECT"]
            "postgresql-core-user"          |   "non_public_schema"         |   "users"     |   ["DELETE", "UPDATE"]    ||  ["DELETE", "UPDATE"]
            "postgresql-core-user"          |   "non_public_schema"         |   "users"     |   ["ALL"]                 ||  ["CREATE", "USAGE"]
            "postgresql-core-user"          |   "non_public_schema"         |   "users"     |   ["ALL PRIVILEGES"]      ||  ["CREATE", "USAGE"]
    }

    def cleanup() {
        for (String privilege : expectedPrivileges) {
            jdbcTemplate.execute("REVOKE " + privilege + " ON SCHEMA " + schema + " FROM  \"" + user + "\";")
            assertEquals("User " + user + " still has privilege " + privilege + " after cleanup", "f", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege)))
        }
    }

    def selectStatement(String user, String table, String schema, String privilege)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT table_catalog, table_schema, table_name, privilege_type ")
        sb.append("FROM   information_schema.table_privileges ")
        sb.append(" WHERE ")
        sb.append(" table_schema = '")
        if (schema == null || schema.trim().isEmpty())
        {
            sb.append("public")
        } else {
            sb.append(schema)
        }
        sb.append("' AND ")
        sb.append(" table_name = '")
        sb.append(table)
        sb.append("' AND ")
        sb.append(" privilege_type = '")
        sb.append(privilege)
        sb.append("' AND ")
        sb.append(" grantee = '")
        sb.append(user)
        sb.append("';")
    }
}
