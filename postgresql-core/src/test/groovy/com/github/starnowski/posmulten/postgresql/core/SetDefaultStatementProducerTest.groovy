package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class SetDefaultStatementProducerTest extends Specification {

    def tested = new SetDefaultStatementProducer()

    @Unroll
    def "should return statement '#expectedStatement' for table '#table' and column '#column', schema '#schema' and default value '#defaultValue'" () {
        expect:
            tested.produce(new SetDefaultStatementProducerParameters(table, column, defaultValue, schema)) == expectedStatement

        where:
            table       |   column      |   defaultValue                                    | schema            ||  expectedStatement
            "users"     |   "tenant_id" |   "current_setting('posmulten.current_tenant')"   | null              ||  "ALTER TABLE users ALTER COLUMN tenant_id SET DEFAULT current_setting('posmulten.current_tenant');"
            "groups"    |   "tenant_id" |   "'value'"                                       | null              ||  "ALTER TABLE groups ALTER COLUMN tenant_id SET DEFAULT 'value';"
            "users"     |   "col1"      |   "current_setting('posmulten.current_tenant')"   | null              ||  "ALTER TABLE users ALTER COLUMN col1 SET DEFAULT current_setting('posmulten.current_tenant');"
            "groups"    |   "col1"      |   "'xxx1'"                                        | null              ||  "ALTER TABLE groups ALTER COLUMN col1 SET DEFAULT 'xxx1';"
            "users"     |   "tenant_id" |   "current_setting('posmulten.current_tenant')"   | "public"          ||  "ALTER TABLE public.users ALTER COLUMN tenant_id SET DEFAULT current_setting('posmulten.current_tenant');"
            "groups"    |   "tenant_id" |   "'value'"                                       | "public"          ||  "ALTER TABLE public.groups ALTER COLUMN tenant_id SET DEFAULT 'value';"
            "users"     |   "col1"      |   "current_setting('posmulten.current_tenant')"   | "public"          ||  "ALTER TABLE public.users ALTER COLUMN col1 SET DEFAULT current_setting('posmulten.current_tenant');"
            "groups"    |   "col1"      |   "'xxx1'"                                        | "public"          ||  "ALTER TABLE public.groups ALTER COLUMN col1 SET DEFAULT 'xxx1';"
            "users"     |   "tenant_id" |   "current_setting('posmulten.current_tenant')"   | "secondary"       ||  "ALTER TABLE secondary.users ALTER COLUMN tenant_id SET DEFAULT current_setting('posmulten.current_tenant');"
            "groups"    |   "tenant_id" |   "'value'"                                       | "secondary"       ||  "ALTER TABLE secondary.groups ALTER COLUMN tenant_id SET DEFAULT 'value';"
            "users"     |   "col1"      |   "current_setting('posmulten.current_tenant')"   | "secondary"       ||  "ALTER TABLE secondary.users ALTER COLUMN col1 SET DEFAULT current_setting('posmulten.current_tenant');"
            "groups"    |   "col1"      |   "'xxx1'"                                        | "secondary"       ||  "ALTER TABLE secondary.groups ALTER COLUMN col1 SET DEFAULT 'xxx1';"
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
    def "should throw exception of type 'IllegalArgumentException' when table name is null, no matter if column name \"#column\" or default value \"#defaultValue\" is correct"()
    {
        when:
            tested.produce(new SetDefaultStatementProducerParameters(null, column, defaultValue, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be null"

        where:
            column          |   defaultValue
            "xx1"           |   "'xdds'"
            "xx1"           |   "current_setting('posmulten.current_tenant')"
            "tenant_id"     |   "'xdds'"
            "tenant_id"     |   "current_setting('posmulten.current_tenant')"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is blank, no matter if column name \"#column\" or default value \"#defaultValue\" is correct"()
    {
        when:
            tested.produce(new SetDefaultStatementProducerParameters(table, column, defaultValue, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be blank"

        where:
            table           | column    |   defaultValue
            ""              | "col1"    |   "'xdds'"
            " "             | "col1"    |   "current_setting('posmulten.current_tenant')"
            "       "       | "col1"    |   "'xdds'"
            ""              | "XXXXXX"  |   "current_setting('posmulten.current_tenant')"
            " "             | "XXXXXX"  |   "current_setting('posmulten.current_tenant')"
            "       "       | "XXXXXX"  |   "'xdds'"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when column name is null, no matter if table name \"#table\" or default value \"#defaultValue\" is correct"()
    {
        when:
            tested.produce(new SetDefaultStatementProducerParameters(table, null, defaultValue, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Column name cannot be null"

        where:
            table           |   defaultValue
            "xx1"           |   "'xdds'"
            "xx1"           |   "current_setting('posmulten.current_tenant')"
            "tenant_id"     |   "'xdds'"
            "tenant_id"     |   "current_setting('posmulten.current_tenant')"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when column name is blank, no matter if table name \"#table\" or default value \"#defaultValue\" is correct"()
    {
        when:
            tested.produce(new SetDefaultStatementProducerParameters(table, column, defaultValue, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Column name cannot be blank"

        where:
            table           | column        |   defaultValue
            "tab"           | ""            |   "'xdds'"
            "tab"           | " "           |   "current_setting('posmulten.current_tenant')"
            "tab"           | "         "   |   "'xdds'"
            "user_groups"   | ""            |   "current_setting('posmulten.current_tenant')"
            "user_groups"   | " "           |   "'xdds'"
            "user_groups"   | "         "   |   "current_setting('posmulten.current_tenant')"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when default value is null, no matter if table name \"#table\" or column name \"#column\" is correct"()
    {
        when:
            tested.produce(new SetDefaultStatementProducerParameters(table, column, null, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Statement for default value cannot be null"

        where:
            table       |   column
            "users"     |   "co1"
            "users"     |   "tenant_id"
            "groups"    |   "'co1'"
            "groups"    |   "tenant_id"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when default value is blank, no matter if table name \"#table\" or column name \"#column\" is correct"()
    {
        when:
            tested.produce(new SetDefaultStatementProducerParameters(table, column, defaultValue, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Statement for default value cannot be blank"

        where:
            table           | defaultValue  |   column
            "tab"           | ""            |   "co1"
            "tab"           | " "           |   "tenant_id"
            "tab"           | "         "   |   "co1"
            "user_groups"   | ""            |   "tenant_id"
            "user_groups"   | " "           |   "co1"
            "user_groups"   | "         "   |   "tenant_id"
    }
}
