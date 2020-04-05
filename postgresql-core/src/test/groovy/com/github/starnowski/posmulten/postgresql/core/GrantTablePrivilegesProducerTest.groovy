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
            null        | "user1"       | "players"     | ["INSERT"]                                                                        || "GRANT INSERT ON 'players' TO 'user1';"
            null        | "john_doe"    | "players"     | ["INSERT"]                                                                        || "GRANT INSERT ON 'players' TO 'john_doe';"
            null        | "user1"       | "users"       | ["INSERT"]                                                                        || "GRANT INSERT ON 'users' TO 'user1';"
            null        | "user1"       | "players"     | ["UPDATE"]                                                                        || "GRANT UPDATE ON 'players' TO 'user1';"
            null        | "user1"       | "players"     | ["SELECT", "INSERT", "UPDATE", "DELETE", "TRUNCATE", "REFERENCES", "TRIGGER"]     || "GRANT SELECT, TRIGGER ON 'players' TO 'user1';"
            null        | "user1"       | "players"     | ["SELECT", "TRIGGER"]                                                             || "GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON 'players' TO 'user1';"
            null        | "user1"       | "players"     | ["ALL"]                                                                           || "GRANT ALL ON 'players' TO 'user1';"
            null        | "user1"       | "players"     | ["ALL PRIVILEGES"]                                                                || "GRANT ALL PRIVILEGES ON 'players' TO 'user1';"
            "public"    | "user1"       | "players"     | ["INSERT"]                                                                        || "GRANT INSERT ON public.'players' TO 'user1';"
            "other_she" | "user1"       | "players"     | ["INSERT"]                                                                        || "GRANT INSERT ON other_she.'players' TO 'user1';"
            "other_she" | "bro"         | "posts"       | ["UPDATE", "SELECT"]                                                              || "GRANT UPDATE, SELECT ON other_she.'posts' TO 'bro';"
    }
}
