package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class CreateColumnStatementProducerTest extends Specification {

    def tested = new CreateColumnStatementProducer()

    @Unroll
    def "should return statement '#expectedStatement' for table '#table' and column '#column' and type '#columnType'" () {
        expect:
            tested.produce(new CreateColumnStatementProducerParameters(table, column, columnType, schema)).getCreateScript() == expectedStatement

        where:
            table       |   column      |   columnType                  | schema            ||  expectedStatement
            "users"     |   "tenant_id" |   "character varying(255)"    | null              ||  "ALTER TABLE users ADD COLUMN tenant_id character varying(255);"
            "groups"    |   "tenant_id" |   "text"                      | null              ||  "ALTER TABLE groups ADD COLUMN tenant_id text;"
            "users"     |   "col1"      |   "character varying(255)"    | null              ||  "ALTER TABLE users ADD COLUMN col1 character varying(255);"
            "groups"    |   "col1"      |   "text"                      | null              ||  "ALTER TABLE groups ADD COLUMN col1 text;"
            "users"     |   "tenant_id" |   "character varying(255)"    | "public"          ||  "ALTER TABLE public.users ADD COLUMN tenant_id character varying(255);"
            "groups"    |   "tenant_id" |   "text"                      | "public"          ||  "ALTER TABLE public.groups ADD COLUMN tenant_id text;"
            "users"     |   "col1"      |   "character varying(255)"    | "public"          ||  "ALTER TABLE public.users ADD COLUMN col1 character varying(255);"
            "groups"    |   "col1"      |   "text"                      | "public"          ||  "ALTER TABLE public.groups ADD COLUMN col1 text;"
            "users"     |   "tenant_id" |   "character varying(255)"    | "secondary"       ||  "ALTER TABLE secondary.users ADD COLUMN tenant_id character varying(255);"
            "groups"    |   "tenant_id" |   "text"                      | "secondary"       ||  "ALTER TABLE secondary.groups ADD COLUMN tenant_id text;"
            "users"     |   "col1"      |   "character varying(255)"    | "secondary"       ||  "ALTER TABLE secondary.users ADD COLUMN col1 character varying(255);"
            "groups"    |   "col1"      |   "text"                      | "secondary"       ||  "ALTER TABLE secondary.groups ADD COLUMN col1 text;"
    }

    @Unroll
    def "should return statement '#expectedStatement' that drops column for table '#table' and column '#column' and type '#columnType'" () {
        expect:
            tested.produce(new CreateColumnStatementProducerParameters(table, column, columnType, schema)).getDropScript() == expectedStatement

        where:
            table       |   column      |   columnType                  | schema            ||  expectedStatement
            "users"     |   "tenant_id" |   "character varying(255)"    | null              ||  "ALTER TABLE users DROP COLUMN tenant_id;"
            "groups"    |   "tenant_id" |   "text"                      | null              ||  "ALTER TABLE groups DROP COLUMN tenant_id;"
            "users"     |   "col1"      |   "character varying(255)"    | null              ||  "ALTER TABLE users DROP COLUMN col1;"
            "groups"    |   "col1"      |   "text"                      | null              ||  "ALTER TABLE groups DROP COLUMN col1;"
            "users"     |   "tenant_id" |   "character varying(255)"    | "public"          ||  "ALTER TABLE public.users DROP COLUMN tenant_id;"
            "groups"    |   "tenant_id" |   "text"                      | "public"          ||  "ALTER TABLE public.groups DROP COLUMN tenant_id;"
            "users"     |   "col1"      |   "character varying(255)"    | "public"          ||  "ALTER TABLE public.users DROP COLUMN col1;"
            "groups"    |   "col1"      |   "text"                      | "public"          ||  "ALTER TABLE public.groups DROP COLUMN col1;"
            "users"     |   "tenant_id" |   "character varying(255)"    | "secondary"       ||  "ALTER TABLE secondary.users DROP COLUMN tenant_id;"
            "groups"    |   "tenant_id" |   "text"                      | "secondary"       ||  "ALTER TABLE secondary.groups DROP COLUMN tenant_id;"
            "users"     |   "col1"      |   "character varying(255)"    | "secondary"       ||  "ALTER TABLE secondary.users DROP COLUMN col1;"
            "groups"    |   "col1"      |   "text"                      | "secondary"       ||  "ALTER TABLE secondary.groups DROP COLUMN col1;"
    }

    @Unroll
    def "should return statement '#expectedStatement' that checks if '#column' exists for table '#table' and schema '#schema' with type '#columnType'" () {
        when:
            def definition = tested.produce(new CreateColumnStatementProducerParameters(table, column, columnType, schema))

        then:
            definition.getCheckingStatements()
            definition.getCheckingStatements().size() == 1
            definition.getCheckingStatements()[0] == expectedStatement

        where:
            table       |   column      |   columnType                  | schema            ||  expectedStatement
            "users"     |   "tenant_id" |   "character varying(255)"    | null              ||  expectedCheckingStatement("users", "tenant_id", null)
            "groups"    |   "tenant_id" |   "text"                      | null              ||  expectedCheckingStatement("groups", "tenant_id", null)
            "users"     |   "col1"      |   "character varying(255)"    | null              ||  expectedCheckingStatement("users", "col1", null)
            "groups"    |   "col1"      |   "text"                      | null              ||  expectedCheckingStatement("groups", "col1", null)
            "users"     |   "tenant_id" |   "character varying(255)"    | "public"          ||  expectedCheckingStatement("users", "tenant_id", "public")
            "groups"    |   "tenant_id" |   "text"                      | "public"          ||  expectedCheckingStatement("groups", "tenant_id", "public")
            "users"     |   "col1"      |   "character varying(255)"    | "public"          ||  expectedCheckingStatement("users", "col1", "public")
            "groups"    |   "col1"      |   "text"                      | "public"          ||  expectedCheckingStatement("groups", "col1", "public")
            "users"     |   "tenant_id" |   "character varying(255)"    | "secondary"       ||  expectedCheckingStatement("users", "tenant_id", "secondary")
            "groups"    |   "tenant_id" |   "text"                      | "secondary"       ||  expectedCheckingStatement("groups", "tenant_id", "secondary")
            "users"     |   "col1"      |   "character varying(255)"    | "secondary"       ||  expectedCheckingStatement("users", "col1", "secondary")
            "groups"    |   "col1"      |   "text"                      | "secondary"       ||  expectedCheckingStatement("groups", "col1", "secondary")
    }

    def "should throw exception of type 'IllegalArgumentException' when parameters object is null" ()
    {
        when:
        tested.produce(null)

        then:
        def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
        ex.message == "The parameters object cannot be null"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is null, no matter if column name \"#column\" or column type \"#columnType\" is correct"()
    {
        when:
            tested.produce(new CreateColumnStatementProducerParameters(null, column, columnType, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be null"

        where:
            column          |   columnType
            "xx1"           |   "character varying(255)"
            "xx1"           |   "text"
            "tenant_id"     |   "character varying(255)"
            "tenant_id"     |   "text"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is blank, no matter if column name \"#column\" or column type \"#columnType\" is correct"()
    {
        when:
            tested.produce(new CreateColumnStatementProducerParameters(table, column, columnType, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be blank"

        where:
            table           | column    |   columnType
            ""              | "col1"    |   "character varying(255)"
            " "             | "col1"    |   "text"
            "       "       | "col1"    |   "character varying(255)"
            ""              | "XXXXXX"  |   "text"
            " "             | "XXXXXX"  |   "text"
            "       "       | "XXXXXX"  |   "character varying(255)"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when column name is null, no matter if table name \"#table\" or column type \"#columnType\" is correct"()
    {
        when:
            tested.produce(new CreateColumnStatementProducerParameters(table, null, columnType, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Column name cannot be null"

        where:
            table           |   columnType
            "xx1"           |   "character varying(255)"
            "xx1"           |   "text"
            "tenant_id"     |   "character varying(255)"
            "tenant_id"     |   "text"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when column name is blank, no matter if table name \"#table\" or column type \"#columnType\" is correct"()
    {
        when:
            tested.produce(new CreateColumnStatementProducerParameters(table, column, columnType, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Column name cannot be blank"

        where:
            table           | column        |   columnType
            "tab"           | ""            |   "character varying(255)"
            "tab"           | " "           |   "text"
            "tab"           | "         "   |   "character varying(255)"
            "user_groups"   | ""            |   "text"
            "user_groups"   | " "           |   "character varying(255)"
            "user_groups"   | "         "   |   "text"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when column type is null, no matter if table name \"#table\" or column name \"#column\" is correct"()
    {
        when:
            tested.produce(new CreateColumnStatementProducerParameters(table, column, null, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Statement for column type cannot be null"

        where:
            table       |   column
            "users"     |   "co1"
            "users"     |   "tenant_id"
            "groups"    |   "'co1'"
            "groups"    |   "tenant_id"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when column type is blank, no matter if table name \"#table\" or column name \"#column\" is correct"()
    {
        when:
            tested.produce(new CreateColumnStatementProducerParameters(table, column, columnType, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Statement for column type cannot be blank"

        where:
            table           | columnType  |   column
            "tab"           | ""            |   "co1"
            "tab"           | " "           |   "tenant_id"
            "tab"           | "         "   |   "co1"
            "user_groups"   | ""            |   "tenant_id"
            "user_groups"   | " "           |   "co1"
            "user_groups"   | "         "   |   "tenant_id"
    }

    private static String expectedCheckingStatement(String table, String column, String schema)
    {
        StringBuilder sb = new StringBuilder()
        sb.append("SELECT COUNT(1) FROM information_schema.columns WHERE ")
        sb.append("table_catalog = 'postgresql_core' AND ")
        if (schema == null)
        {
            sb.append("table_schema = 'public'")
        } else {
            sb.append("table_schema = '")
            sb.append(schema)
            sb.append("'")
        }
        sb.append(" AND ")
        sb.append("table_name = '")
        sb.append(table)
        sb.append("' AND ")
        sb.append("column_name = '")
        sb.append(column)
        sb.append("'")
        sb.toString()
    }
}
