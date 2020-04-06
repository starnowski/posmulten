package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class GrantTablePrivilegesProducerTest extends Specification {

    def tested = new GrantTablePrivilegesProducer()

    @Unroll
    def "should return granting access statement '#expectedStatement' for table '#table', user '#user', schema '#schema' with specified privileges '#privileges'" () {
        expect:
            tested.produce(schema, table, user, privileges) == expectedStatement

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
            ex.message == "privileges list cannot be null"

        where:
            schema      | user          | table
            null        | "user1"       | "players"
            null        | "john_doe"    | "players"
            null        | "user1"       | "users"
            "other_she" | "user1"       | "players"
            "other_she" | "bro"         | "posts"
    }
}
