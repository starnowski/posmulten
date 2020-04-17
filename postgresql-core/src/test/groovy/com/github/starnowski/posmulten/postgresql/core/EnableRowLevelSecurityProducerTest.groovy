package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class EnableRowLevelSecurityProducerTest extends Specification {

    def tested = new EnableRowLevelSecurityProducer()

    @Unroll
    def "should return statement (#expectedStatement) that enables row level security mode for table (#table) and schema (#schema)"()
    {
        expect:
            tested.produce(schema, table) == expectedStatement
        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  "ALTER TABLE \"users\" ENABLE ROW LEVEL SECURITY;"
            null        | "posts"   ||  "ALTER TABLE \"posts\" ENABLE ROW LEVEL SECURITY;"
            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" ENABLE ROW LEVEL SECURITY;"
            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" ENABLE ROW LEVEL SECURITY;"
    }

}
