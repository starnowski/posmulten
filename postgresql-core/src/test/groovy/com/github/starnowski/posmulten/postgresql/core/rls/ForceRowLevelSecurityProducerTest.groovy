package com.github.starnowski.posmulten.postgresql.core.rls

import spock.lang.Specification
import spock.lang.Unroll

class ForceRowLevelSecurityProducerTest extends Specification {

    def tested = new ForceRowLevelSecurityProducer()

    @Unroll
    def "should return statement (#expectedStatement) that forces row level security mode for table (#table) and schema (#schema)"()
    {
        expect:
            tested.produce(table, schema).getCreateScript() == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  "ALTER TABLE \"users\" FORCE ROW LEVEL SECURITY;"
            null        | "posts"   ||  "ALTER TABLE \"posts\" FORCE ROW LEVEL SECURITY;"
            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" FORCE ROW LEVEL SECURITY;"
            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" FORCE ROW LEVEL SECURITY;"
            "public"    | "posts"   ||  "ALTER TABLE public.\"posts\" FORCE ROW LEVEL SECURITY;"
    }

    @Unroll
    def "should return statement (#expectedStatement) that drops row level security mode for table (#table) and schema (#schema)"()
    {
        expect:
            tested.produce(table, schema).getDropScript() == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  "ALTER TABLE \"users\" NO FORCE ROW LEVEL SECURITY;"
            null        | "posts"   ||  "ALTER TABLE \"posts\" NO FORCE ROW LEVEL SECURITY;"
            "secondary" | "users"   ||  "ALTER TABLE secondary.\"users\" NO FORCE ROW LEVEL SECURITY;"
            "secondary" | "posts"   ||  "ALTER TABLE secondary.\"posts\" NO FORCE ROW LEVEL SECURITY;"
            "public"    | "posts"   ||  "ALTER TABLE public.\"posts\" NO FORCE ROW LEVEL SECURITY;"
    }

    @Unroll
    def "should return checking statement (#expectedStatement) for table (#table) and schema (#schema)"()
    {
        when:
            def sqlDefinition = tested.produce(table, schema)

        then:
            sqlDefinition.getCheckingStatements()
            sqlDefinition.getCheckingStatements().size() == 1
            sqlDefinition.getCheckingStatements()[0] == expectedStatement

        where:
            schema      | table     ||	expectedStatement
            null        | "users"   ||  prepareCheckingStatements(null, "users")
            null        | "posts"   ||  prepareCheckingStatements(null, "posts")
            "secondary" | "users"   ||  prepareCheckingStatements("secondary", "users")
            "secondary" | "posts"   ||  prepareCheckingStatements("secondary", "posts")
            "public"    | "posts"   ||  prepareCheckingStatements("public", "posts")
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

    private static String prepareCheckingStatements(String schema, String table)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT COUNT(1) FROM pg_class pc, pg_catalog.pg_namespace pg ")
        sb.append("WHERE")
        sb.append(" pc.relname = '")
        sb.append(table)
        sb.append("' AND pc.relnamespace = pg.oid AND pg.nspname = '")
        if (schema == null)
            sb.append("public")
        else
            sb.append(schema)
        sb.append("' AND pc.relforcerowsecurity = 't';")
        sb.toString()
    }
}
