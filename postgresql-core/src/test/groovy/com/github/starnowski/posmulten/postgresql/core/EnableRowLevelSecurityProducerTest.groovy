package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class EnableRowLevelSecurityProducerTest extends Specification {

    def tested = new EnableRowLevelSecurityProducer()

    @Unroll
    def "should return statement (#expectedStatement) that enables row level security mode for table (#table) and schema (#schema)"()
    {
        expect:
            tested.produce(table, schema) == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  "ALTER TABLE \"users\" ENABLE ROW LEVEL SECURITY;"
            null        | "posts"   ||  "ALTER TABLE \"posts\" ENABLE ROW LEVEL SECURITY;"
            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" ENABLE ROW LEVEL SECURITY;"
            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" ENABLE ROW LEVEL SECURITY;"
            "public"    | "posts"      ||  "ALTER TABLE public.\"posts\" ENABLE ROW LEVEL SECURITY;"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when schema name is blank '#schema', no matter if table \"#table\" is correct"()
    {
        when:
            tested.produce(table, schema)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Schema name cannot be blank"

        where:
            table               |   schema
            "users"             |   ""
            "users"             |   "   "
            "posts"             |   ""
            "posts"             |   "   "
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is blank '#table', no matter if schema \"#schema\" is correct"()
    {
        when:
        tested.produce(table, schema)

        then:
        def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
        ex.message == "Table name cannot be blank"

        where:
            schema          |   table
            "public"        |   ""
            "public"        |   "   "
            "secondary"     |   ""
            "secondary"     |   "   "
    }
}
