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
class GrantSchemaPrivilegesProducerItTest extends Specification {

    @Autowired
    JdbcTemplate jdbcTemplate
    @Autowired
    SqlUtils sqlUtils

    def tested = new GrantSchemaPrivilegesProducer()

    String user
    String schema
    String[] privileges
    String[] expectedPrivileges
    SQLDefinition sqlDefinition

    @Unroll
    def "should add \"#testPrivileges\" privileges to \"#testUser\" user for \"#testSchema\" schema" () {
        given:
            user = testUser
            schema = testSchema
            privileges = testPrivileges
            expectedPrivileges = testExpectedPrivileges
            for (String privilege : testExpectedPrivileges) {
                assertEquals("User " + testUser + " has privilege " + privilege, "f", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege)))
            }

        when:
            sqlDefinition = tested.produce(testSchema, testUser, testPrivileges)
            sqlUtils.assertAllResultForCheckingStatementsAreEqualZero(sqlDefinition)
            jdbcTemplate.execute(sqlDefinition.getCreateScript())

        then:
            for (String privilege : testExpectedPrivileges) {
                assertEquals("User " + testUser + " does not have privilege " + privilege, "t", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege)))
            }
            sqlUtils.assertAllCheckingStatementsArePassing(sqlDefinition)

        where:
            testUser                        |   testSchema      |   testPrivileges          ||  testExpectedPrivileges
            "postgresql-core-user"          |   "non_public_schema"        |   ["USAGE"]               ||  ["USAGE"]
            "postgresql-core-user"          |   "non_public_schema"        |   ["CREATE"]              ||  ["CREATE"]
            "postgresql-core-user"          |   "non_public_schema"        |   ["CREATE", "USAGE"]     ||  ["CREATE", "USAGE"]
            "postgresql-core-user"          |   "non_public_schema"        |   ["ALL"]                 ||  ["CREATE", "USAGE"]
            "postgresql-core-user"          |   "non_public_schema"        |   ["ALL PRIVILEGES"]      ||  ["CREATE", "USAGE"]
    }

    def cleanup() {
        jdbcTemplate.execute(sqlDefinition.getDropScript())
        for (String privilege : expectedPrivileges) {
            assertEquals("User " + user + " still has privilege " + privilege + " after cleanup", "f", selectAndReturnFirstRecordAsString(jdbcTemplate, selectStatement(user, schema, privilege)))
        }
    }

    def selectStatement(String user, String schema, String privilege)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT pg_catalog.has_schema_privilege('")
        sb.append(user)
        sb.append("', '")
        sb.append(schema)
        sb.append("', '")
        sb.append(privilege)
        sb.append("') AS USER_HAS_PRIVILEGE;")
        sb.toString()
    }
}
