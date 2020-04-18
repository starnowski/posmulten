package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class ForceRowLevelSecurityProducerTest extends Specification {

    def tested = new ForceRowLevelSecurityProducer()

    @Unroll
    def "should return statement (#expectedStatement) that forces row level security mode for table (#table) and schema (#schema)"()
    {
        expect:
            tested.produce(table, schema) == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  "ALTER TABLE \"users\" FORCE ROW LEVEL SECURITY;"
            null        | "posts"   ||  "ALTER TABLE \"posts\" FORCE ROW LEVEL SECURITY;"
            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" FORCE ROW LEVEL SECURITY;"
            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" FORCE ROW LEVEL SECURITY;"
            "public"    | "posts"   ||  "ALTER TABLE public.\"posts\" FORCE ROW LEVEL SECURITY;"
    }
}
