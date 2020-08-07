package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class SetNotNullStatementProducerTest extends Specification {

    def tested = new SetNotNullStatementProducer()

    @Unroll
    def "should return statement '#expectedStatement' for table '#table', schema '#schema' and column '#column'" () {
        expect:
            tested.produce(new SetNotNullStatementProducerParameters(table, column, schema)).getCreateScript() == expectedStatement

        where:
            table       |   column      | schema            ||  expectedStatement
            "users"     |   "tenant_id" |   null            ||  "ALTER TABLE users ALTER COLUMN tenant_id SET NOT NULL;"
            "groups"    |   "tenant_id" |   null            ||  "ALTER TABLE groups ALTER COLUMN tenant_id SET NOT NULL;"
            "users"     |   "col1"      |   null            ||  "ALTER TABLE users ALTER COLUMN col1 SET NOT NULL;"
            "groups"    |   "col1"      |   null            ||  "ALTER TABLE groups ALTER COLUMN col1 SET NOT NULL;"
            "users"     |   "tenant_id" |   "public"        ||  "ALTER TABLE public.users ALTER COLUMN tenant_id SET NOT NULL;"
            "groups"    |   "tenant_id" |   "public"        ||  "ALTER TABLE public.groups ALTER COLUMN tenant_id SET NOT NULL;"
            "users"     |   "col1"      |   "public"        ||  "ALTER TABLE public.users ALTER COLUMN col1 SET NOT NULL;"
            "groups"    |   "col1"      |   "public"        ||  "ALTER TABLE public.groups ALTER COLUMN col1 SET NOT NULL;"
            "users"     |   "tenant_id" |   "secondary"     ||  "ALTER TABLE secondary.users ALTER COLUMN tenant_id SET NOT NULL;"
            "groups"    |   "tenant_id" |   "secondary"     ||  "ALTER TABLE secondary.groups ALTER COLUMN tenant_id SET NOT NULL;"
            "users"     |   "col1"      |   "secondary"     ||  "ALTER TABLE secondary.users ALTER COLUMN col1 SET NOT NULL;"
            "groups"    |   "col1"      |   "secondary"     ||  "ALTER TABLE secondary.groups ALTER COLUMN col1 SET NOT NULL;"
    }

    @Unroll
    def "should return statement '#expectedStatement' for table '#table', schema '#schema' and column '#column' that drops the NOT NULL constraint" () {
        expect:
            tested.produce(new SetNotNullStatementProducerParameters(table, column, schema)).getDropScript() == expectedStatement

        where:
            table       |   column      | schema            ||  expectedStatement
            "users"     |   "tenant_id" |   null            ||  "ALTER TABLE users ALTER COLUMN tenant_id DROP NOT NULL;"
            "groups"    |   "tenant_id" |   null            ||  "ALTER TABLE groups ALTER COLUMN tenant_id DROP NOT NULL;"
            "users"     |   "col1"      |   null            ||  "ALTER TABLE users ALTER COLUMN col1 DROP NOT NULL;"
            "groups"    |   "col1"      |   null            ||  "ALTER TABLE groups ALTER COLUMN col1 DROP NOT NULL;"
            "users"     |   "tenant_id" |   "public"        ||  "ALTER TABLE public.users ALTER COLUMN tenant_id DROP NOT NULL;"
            "groups"    |   "tenant_id" |   "public"        ||  "ALTER TABLE public.groups ALTER COLUMN tenant_id DROP NOT NULL;"
            "users"     |   "col1"      |   "public"        ||  "ALTER TABLE public.users ALTER COLUMN col1 DROP NOT NULL;"
            "groups"    |   "col1"      |   "public"        ||  "ALTER TABLE public.groups ALTER COLUMN col1 DROP NOT NULL;"
            "users"     |   "tenant_id" |   "secondary"     ||  "ALTER TABLE secondary.users ALTER COLUMN tenant_id DROP NOT NULL;"
            "groups"    |   "tenant_id" |   "secondary"     ||  "ALTER TABLE secondary.groups ALTER COLUMN tenant_id DROP NOT NULL;"
            "users"     |   "col1"      |   "secondary"     ||  "ALTER TABLE secondary.users ALTER COLUMN col1 DROP NOT NULL;"
            "groups"    |   "col1"      |   "secondary"     ||  "ALTER TABLE secondary.groups ALTER COLUMN col1 DROP NOT NULL;"
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
    def "should throw exception of type 'IllegalArgumentException' when table name is null, no matter if column name is correct \"#column\""()
    {
        when:
            tested.produce(new SetNotNullStatementProducerParameters(null, column, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be null"

        where:
            column << ["xxx", "col1", "test"]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is blank, no matter if column name is correct \"#column\""()
    {
        when:
            tested.produce(new SetNotNullStatementProducerParameters(table, column, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Table name cannot be blank"

        where:
            table           | column
            ""              | "col1"
            " "             | "col1"
            "       "       | "col1"
            ""              | "XXXXXX"
            " "             | "XXXXXX"
            "       "       | "XXXXXX"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when column name is null, no matter if table name is correct \"#table\""()
    {
        when:
            tested.produce(new SetNotNullStatementProducerParameters(table, null, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Column name cannot be null"

        where:
            table << ["xxx", "col1", "test"]
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when column name is blank, no matter if table name is correct \"#table\""()
    {
        when:
            tested.produce(new SetNotNullStatementProducerParameters(table, column, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Column name cannot be blank"

        where:
            table           | column
            "tab"           | ""
            "tab"           | " "
            "tab"           | "         "
            "user_groups"   | ""
            "user_groups"   | " "
            "user_groups"   | "         "
    }
}
