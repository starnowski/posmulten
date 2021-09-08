package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

import static java.util.Arrays.asList
import static java.util.stream.Collectors.toList

class GrantTablePrivilegesProducerTest extends Specification {

    def tested = new GrantTablePrivilegesProducer()

    @Unroll
    def "should return granting access statement '#expectedStatement' for table '#table', user '#user', schema '#schema' with specified privileges '#privileges'" () {
        expect:
            tested.produce(schema, table, user, privileges).getCreateScript() == expectedStatement

        where:
            schema      | user          | table         | privileges                                                                        ||	expectedStatement
            null        | "user1"       | "players"     | ["INSERT"]                                                                        || "GRANT INSERT ON \"players\" TO \"user1\";"
            null        | "john_doe"    | "players"     | ["INSERT"]                                                                        || "GRANT INSERT ON \"players\" TO \"john_doe\";"
            null        | "user1"       | "users"       | ["INSERT"]                                                                        || "GRANT INSERT ON \"users\" TO \"user1\";"
            null        | "user1"       | "players"     | ["UPDATE"]                                                                        || "GRANT UPDATE ON \"players\" TO \"user1\";"
            null        | "user1"       | "players"     | ["SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER"]     || "GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON \"players\" TO \"user1\";"
            null        | "user1"       | "players"     | ["SELECT", "TRIGGER"]                                                             || "GRANT SELECT, TRIGGER ON \"players\" TO \"user1\";"
            null        | "user1"       | "players"     | ["ALL"]                                                                           || "GRANT ALL ON \"players\" TO \"user1\";"
            null        | "user1"       | "players"     | ["ALL PRIVILEGES"]                                                                || "GRANT ALL PRIVILEGES ON \"players\" TO \"user1\";"
            "public"    | "user1"       | "players"     | ["INSERT"]                                                                        || "GRANT INSERT ON public.\"players\" TO \"user1\";"
            "other_she" | "user1"       | "players"     | ["INSERT"]                                                                        || "GRANT INSERT ON other_she.\"players\" TO \"user1\";"
            "other_she" | "bro"         | "posts"       | ["UPDATE", "SELECT"]                                                              || "GRANT UPDATE, SELECT ON other_she.\"posts\" TO \"bro\";"
    }

    @Unroll
    def "should return revoking access statement '#expectedStatement' for table '#table', user '#user', schema '#schema' with specified privileges '#privileges'" () {
        expect:
        tested.produce(schema, table, user, privileges).getDropScript() == expectedStatement

        where:
        schema      | user          | table         | privileges                                                                        ||	expectedStatement
        null        | "user1"       | "players"     | ["INSERT"]                                                                        || "REVOKE INSERT ON \"players\" FROM \"user1\";"
        null        | "john_doe"    | "players"     | ["INSERT"]                                                                        || "REVOKE INSERT ON \"players\" FROM \"john_doe\";"
        null        | "user1"       | "users"       | ["INSERT"]                                                                        || "REVOKE INSERT ON \"users\" FROM \"user1\";"
        null        | "user1"       | "players"     | ["UPDATE"]                                                                        || "REVOKE UPDATE ON \"players\" FROM \"user1\";"
        null        | "user1"       | "players"     | ["SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER"]     || "REVOKE SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON \"players\" FROM \"user1\";"
        null        | "user1"       | "players"     | ["SELECT", "TRIGGER"]                                                             || "REVOKE SELECT, TRIGGER ON \"players\" FROM \"user1\";"
        null        | "user1"       | "players"     | ["ALL"]                                                                           || "REVOKE ALL ON \"players\" FROM \"user1\";"
        null        | "user1"       | "players"     | ["ALL PRIVILEGES"]                                                                || "REVOKE ALL PRIVILEGES ON \"players\" FROM \"user1\";"
        "public"    | "user1"       | "players"     | ["INSERT"]                                                                        || "REVOKE INSERT ON public.\"players\" FROM \"user1\";"
        "other_she" | "user1"       | "players"     | ["INSERT"]                                                                        || "REVOKE INSERT ON other_she.\"players\" FROM \"user1\";"
        "other_she" | "bro"         | "posts"       | ["UPDATE", "SELECT"]                                                              || "REVOKE UPDATE, SELECT ON other_she.\"posts\" FROM \"bro\";"
    }

    @Unroll
    def "should return checking statements '#expectedStatements' for table '#table', user '#user', schema '#schema' with specified privileges '#privileges'" () {
        when:
            def definition = tested.produce(schema, table, user, privileges)

        then:
            definition.getCheckingStatements()
            definition.getCheckingStatements().size() == expectedStatements.size()
            definition.getCheckingStatements().containsAll(expectedStatements)

        where:
            schema      | user          | table         | privileges                                                                        ||	expectedStatements
            null        | "user1"       | "players"     | ["INSERT"]                                                                        || chSts("user1", "players", null, "INSERT")
            null        | "john_doe"    | "players"     | ["INSERT"]                                                                        || chSts("john_doe", "players", null, "INSERT")
            null        | "user1"       | "users"       | ["INSERT"]                                                                        || chSts("user1", "users", null, "INSERT")
            null        | "user1"       | "players"     | ["UPDATE"]                                                                        || chSts("user1", "players", null, "UPDATE")
            null        | "user1"       | "players"     | ["SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER"]     || chSts("user1", "players", null, "SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER")
            null        | "user1"       | "players"     | ["SELECT", "TRIGGER"]                                                             || chSts("user1", "players", null, "SELECT", "TRIGGER")
            null        | "user1"       | "players"     | ["ALL"]                                                                           || chSts("user1", "players", null, "SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER")
            null        | "user1"       | "players"     | ["ALL PRIVILEGES"]                                                                || chSts("user1", "players", null, "SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER")
            "public"    | "user1"       | "players"     | ["INSERT"]                                                                        || chSts("user1", "players", "public", "INSERT")
            "other_she" | "user1"       | "players"     | ["INSERT"]                                                                        || chSts("user1", "players", "other_she", "INSERT")
            "other_she" | "bro"         | "posts"       | ["UPDATE", "SELECT"]                                                              || chSts("bro", "posts", "other_she", "UPDATE", "SELECT")
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is null"()
    {
        when:
            tested.produce(schema, null, user, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be null"

        where:
            schema      | user          |   privileges
            null        | "user1"       |   ["INSERT"]
            null        | "john_doe"    |   ["INSERT"]
            null        | "user1"       |   ["INSERT"]
            null        | "user1"       |   ["UPDATE"]
            null        | "user1"       |   ["SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER"]
            null        | "user1"       |   ["SELECT", "TRIGGER"]
            null        | "user1"       |   ["ALL"]
            null        | "user1"       |   ["ALL PRIVILEGES"]
            "public"    | "user1"       |   ["INSERT"]
            "other_she" | "user1"       |   ["INSERT"]
            "other_she" | "bro"         |   ["UPDATE", "SELECT"]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is blank"()
    {
        when:
            tested.produce(schema, table, user, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be blank"

        where:
            schema      | user          |   privileges              |   table
            null        | "user1"       |   ["INSERT"]              | ""
            null        | "john_doe"    |   ["INSERT"]              | "   "
            null        | "user1"       |   ["INSERT"]              | "  "
            null        | "user1"       |   ["UPDATE"]              | "     "
            null        | "user1"       |   ["SELECT", "INSERT"]    | "  "
            null        | "user1"       |   ["SELECT", "TRIGGER"]   | "   "
            null        | "user1"       |   ["ALL"]                 | "   "
            null        | "user1"       |   ["ALL PRIVILEGES"]      | "  "
            "public"    | "user1"       |   ["INSERT"]              | " "
            "other_she" | "user1"       |   ["INSERT"]              | "    "
            "other_she" | "bro"         |   ["UPDATE", "SELECT"]    | "        "
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when user name is null"()
    {
        when:
            tested.produce(schema, table, null, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "user cannot be null"

        where:
            schema      | table         |   privileges
            null        | "users"       |   ["INSERT"]
            null        | "posts"       |   ["INSERT"]
            null        | "posts"       |   ["INSERT"]
            null        | "posts"       |   ["UPDATE"]
            null        | "posts"       |   ["SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER"]
            null        | "posts"       |   ["SELECT", "TRIGGER"]
            null        | "posts"       |   ["ALL"]
            null        | "posts"       |   ["ALL PRIVILEGES"]
            "public"    | "posts"       |   ["INSERT"]
            "other_she" | "posts"       |   ["INSERT"]
            "other_she" | "posts"       |   ["UPDATE", "SELECT"]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table user is blank"()
    {
        when:
            tested.produce(schema, table, user, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "user cannot be blank"

        where:
            schema      | table         |   privileges              |   user
            null        | "posts"       |   ["INSERT"]              | ""
            null        | "users"       |   ["INSERT"]              | "   "
            null        | "posts"       |   ["INSERT"]              | "  "
            null        | "posts"       |   ["UPDATE"]              | "     "
            null        | "posts"       |   ["SELECT", "INSERT"]    | "  "
            null        | "posts"       |   ["SELECT", "TRIGGER"]   | "   "
            null        | "posts"       |   ["ALL"]                 | "   "
            null        | "posts"       |   ["ALL PRIVILEGES"]      | "  "
            "public"    | "posts"       |   ["INSERT"]              | " "
            "other_she" | "posts"       |   ["INSERT"]              | "    "
            "other_she" | "posts"       |   ["UPDATE", "SELECT"]    | "        "
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table privileges list is null"()
    {
        when:
            tested.produce(schema, table, user, null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "privileges list cannot be null"

        where:
            schema      | user          | table
            null        | "user1"       | "players"
            null        | "john_doe"    | "players"
            null        | "user1"       | "users"
            "other_she" | "user1"       | "players"
            "other_she" | "bro"         | "posts"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table privileges list is empty"()
    {
        when:
            tested.produce(schema, table, user, new ArrayList<String>())

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "privileges list cannot be empty"

        where:
            schema      | user          | table
            null        | "user1"       | "players"
            null        | "john_doe"    | "players"
            null        | "user1"       | "users"
            "other_she" | "user1"       | "players"
            "other_she" | "bro"         | "posts"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table schema is blank"()
    {
        when:
            tested.produce(schema, table, user, privileges)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "schema cannot be blank"

        where:
        user      | table         |   privileges              |   schema
        "user1"        | "posts"       |   ["INSERT"]              | ""
        "user1"        | "users"       |   ["UPDATE"]              | "   "
    }

    private static String chSt(String user, String table, String schema, String privilege)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT COUNT(1) ")
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
        sb.append("'")
        sb.toString()
    }

    private static List<String> chSts(String user, String table, String schema, String... privileges)
    {
        asList(privileges).stream().map({p -> chSt(user, table, schema, p)}).collect(toList())
    }
}
