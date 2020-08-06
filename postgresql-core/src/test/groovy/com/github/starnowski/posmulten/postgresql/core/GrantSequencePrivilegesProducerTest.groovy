package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class GrantSequencePrivilegesProducerTest extends Specification {

    def tested = new GrantSequencePrivilegesProducer()

    @Unroll
    def "should return granting access statement '#expectedStatement' for sequence #sequence, user '#user', schema '#schema' with 'ALL PRIVILGES'" () {
        expect:
            tested.produce(schema, sequence, user).getCreateScript() == expectedStatement

        where:
            schema                  | user          | sequence      ||	expectedStatement
            null                    | "user1"       | "primary"     || "GRANT ALL PRIVILEGES ON SEQUENCE public.\"primary\" TO \"user1\";"
            "public"                | "user1"       | "primary"     || "GRANT ALL PRIVILEGES ON SEQUENCE public.\"primary\" TO \"user1\";"
            "non_public_schema"     | "johndoe"     | "primary"     || "GRANT ALL PRIVILEGES ON SEQUENCE non_public_schema.\"primary\" TO \"johndoe\";"
            "non_public_schema"     | "johndoe"     | "secondary"   || "GRANT ALL PRIVILEGES ON SEQUENCE non_public_schema.\"secondary\" TO \"johndoe\";"
    }

    @Unroll
    def "should return revoking access statement '#expectedStatement' for sequence #sequence, user '#user', schema '#schema' with 'ALL PRIVILGES'" () {
        expect:
            tested.produce(schema, sequence, user).getDropScript() == expectedStatement

        where:
            schema                  | user          | sequence      ||	expectedStatement
            null                    | "user1"       | "primary"     || "REVOKE ALL PRIVILEGES ON SEQUENCE public.\"primary\" FROM \"user1\";"
            "public"                | "user1"       | "primary"     || "REVOKE ALL PRIVILEGES ON SEQUENCE public.\"primary\" FROM \"user1\";"
            "non_public_schema"     | "johndoe"     | "primary"     || "REVOKE ALL PRIVILEGES ON SEQUENCE non_public_schema.\"primary\" FROM \"johndoe\";"
            "non_public_schema"     | "johndoe"     | "secondary"   || "REVOKE ALL PRIVILEGES ON SEQUENCE non_public_schema.\"secondary\" FROM \"johndoe\";"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table schema is blank"()
    {
        when:
            tested.produce(schema, sequence, user)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "schema cannot be blank"

        where:
            user        | sequence      |schema
            "user1"     | "primary"     |""
            "user1"     | "secondary"   |"   "
            "john_doe"  | "primary"     |""
            "john_doe"  | "secondary"   |"   "
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when user name is null"()
    {
        when:
            tested.produce(schema, sequence, null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "user cannot be null"

        where:
        schema              | sequence
            "public"        | "primary"
            "public"        | "secondary"
            "secondary"     | "primary"
            "secondary"     | "secondary"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table user is blank"()
    {
        when:
            tested.produce(schema, sequence, user)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "user cannot be blank"

        where:
            schema          | sequence      |   user
            "public"        | "primary"     |   ""
            "public"        | "secondary"   |   "   "
            "secondary"     | "primary"     |   ""
            "secondary"     | "secondary"   |   "   "
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when sequence is null"()
    {
        when:
            tested.produce(schema, null, user)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "sequence cannot be null"

        where:
            schema          | user
            "public"        | "postgres"
            "public"        | "user1"
            "secondary"     | "postgres"
            "secondary"     | "user1"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when sequence is blank"()
    {
        when:
        tested.produce(schema, sequence, user)

        then:
        def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
        ex.message == "sequence cannot be blank"

        where:
        schema          | user          |   sequence
        "public"        | "postgres"    |   ""
        "public"        | "user1"       |   "   "
        "secondary"     | "postgres"    |   ""
        "secondary"     | "user1"       |   "   "
    }

}
