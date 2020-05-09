package com.github.starnowski.posmulten.postgresql.core.rls

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

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is null '#table', no matter if schema \"#schema\" is correct"()
    {
        when:
            tested.produce(null, schema)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be null"

        where:
            schema  << ["public", "secondary"]
    }
}
