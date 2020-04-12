package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class GrantSequencePrivilegesProducerTest extends Specification {

    def tested = new GrantSequencePrivilegesProducer()

    @Unroll
    def "should return granting access statement '#expectedStatement' for sequence #sequence, user '#user', schema '#schema' with 'ALL PRIVILGES'" () {
        expect:
            tested.produce(schema, sequence, user) == expectedStatement

        where:
            schema                  | user          | sequence      ||	expectedStatement
            null                    | "user1"       | "primary"     || "GRANT ALL PRIVILEGES ON SEQUENCE public.\"primary\" TO \"user1\";"
            "public"                | "user1"       | "primary"     || "GRANT ALL PRIVILEGES ON SEQUENCE public.\"primary\" TO \"user1\";"
            "non_public_schema"     | "johndoe"     | "primary"     || "GRANT ALL PRIVILEGES ON SEQUENCE non_public_schema.\"primary\" TO \"johndoe\";"
            "non_public_schema"     | "johndoe"     | "secondary"   || "GRANT ALL PRIVILEGES ON SEQUENCE non_public_schema.\"secondary\" TO \"johndoe\";"
    }

}
