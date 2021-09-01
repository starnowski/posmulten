package com.github.starnowski.posmulten.postgresql.core.rls

import spock.lang.Specification
import spock.lang.Unroll

class EnableRowLevelSecurityProducerTest extends Specification {

    def tested = new EnableRowLevelSecurityProducer()

    @Unroll
    def "should return statement (#expectedStatement) that enables row level security mode for table (#table) and schema (#schema)"()
    {
        expect:
            tested.produce(table, schema).getCreateScript() == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  "ALTER TABLE \"users\" ENABLE ROW LEVEL SECURITY;"
            null        | "posts"   ||  "ALTER TABLE \"posts\" ENABLE ROW LEVEL SECURITY;"
            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" ENABLE ROW LEVEL SECURITY;"
            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" ENABLE ROW LEVEL SECURITY;"
            "public"    | "posts"   ||  "ALTER TABLE public.\"posts\" ENABLE ROW LEVEL SECURITY;"
    }

    @Unroll
    def "should return statement (#expectedStatement) that drops row level security mode for table (#table) and schema (#schema)"()
    {
        expect:
            tested.produce(table, schema).getDropScript() == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  "ALTER TABLE \"users\" DISABLE ROW LEVEL SECURITY;"
            null        | "posts"   ||  "ALTER TABLE \"posts\" DISABLE ROW LEVEL SECURITY;"
            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" DISABLE ROW LEVEL SECURITY;"
            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" DISABLE ROW LEVEL SECURITY;"
            "public"    | "posts"   ||  "ALTER TABLE public.\"posts\" DISABLE ROW LEVEL SECURITY;"
    }

    @Unroll
    def "should return statement (#expectedStatement) that checks if row level security mode for table (#table) and schema (#schema) is applied"()
    {
        when:
            def sqlDefinition = tested.produce(table, schema)

        then:
            sqlDefinition.getCheckingStatements().size() == 1
            sqlDefinition.getCheckingStatements()[0] == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  expectedCheckingStatement(null, "users")
            null        | "posts"   ||  expectedCheckingStatement(null, "posts")
            "secondary" | "users"   ||  expectedCheckingStatement("secondary", "users")
            "secondary" | "posts"   ||  expectedCheckingStatement("secondary", "posts")
            "public"    | "posts"   ||  expectedCheckingStatement("public", "posts")
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

    private static String expectedCheckingStatement(String schema, String table)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT pc.relrowsecurity FROM pg_class pc, pg_catalog.pg_namespace pg ")
        sb.append("WHERE")
        sb.append(" pc.relname = '")
        sb.append(table)
        sb.append("' AND pc.relnamespace = pg.oid AND pg.nspname = '")
        if (schema == null)
            sb.append("public")
        else
            sb.append(schema)
        sb.append("';")
        sb.toString()
    }
}
