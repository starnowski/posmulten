package com.github.starnowski.posmulten.postgresql.core

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.util.SqlUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.TestUtils.selectAndReturnFirstRecordAsString

import static org.junit.Assert.assertEquals

@SpringBootTest(classes = [TestApplication.class])
class GrantSequencePrivilegesProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    SqlUtils sqlUtils

    def tested = new GrantSequencePrivilegesProducer()

    String user
    String schema
    String sequence
    String[] expectedPrivileges
    SQLDefinition sqlDefinition

    @Unroll
    def "should grant all privileges for sequence #testSequence in schema '#testSchema' to user '#testUser''" () {
        given:
            user = testUser
            schema = testSchema
            sequence = testSequence
            expectedPrivileges = testExpectedPrivileges
            for (String privilege : testExpectedPrivileges) {
                assertEquals("User " + testUser + " has privilege " + privilege, "f", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege, sequence)))
            }

        when:
            sqlDefinition = tested.produce(schema, sequence, user)
            sqlUtils.assertAllResultForCheckingStatementsAreEqualZero(sqlDefinition)
            jdbcTemplate.execute(sqlDefinition.getCreateScript())

        then:
            for (String privilege : testExpectedPrivileges) {
                assertEquals("User " + testUser + " don't have privilege " + privilege, "t", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege, sequence)))
            }
            sqlUtils.assertAllCheckingStatementsArePassing(sqlDefinition)

        where:
            testSchema              | testUser                  | testSequence          ||  testExpectedPrivileges
            "public"                | "postgresql-core-user"    | "primary_sequence"    ||  ["USAGE"]
            "public"                | "postgresql-core-user"    | "primary_sequence"    ||  ["USAGE"]
            "non_public_schema"     | "postgresql-core-user"    | "primary_sequence"    ||  ["USAGE"]
            "non_public_schema"     | "postgresql-core-user"    | "primary_sequence"    ||  ["USAGE"]
    }

    def cleanup() {
        jdbcTemplate.execute(sqlDefinition.getDropScript())
        for (String privilege : expectedPrivileges) {
            assertEquals("User " + user + " still has privilege " + privilege + " after cleanup", "f", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege, sequence)))
        }
    }

    def selectStatement(String user, String schema, String privilege, String sequence)
    {
        def pattern = "SELECT EXISTS(SELECT * FROM   information_schema.usage_privileges WHERE  grantee = '%1s' AND object_schema = '%2s' AND object_name = '%3s' AND object_type = 'SEQUENCE' AND privilege_type = '%4s');"
        String.format(pattern, user, schema, sequence, privilege)
    }
}
