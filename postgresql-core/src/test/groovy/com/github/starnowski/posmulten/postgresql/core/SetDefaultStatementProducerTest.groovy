package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class SetDefaultStatementProducerTest extends Specification {

    def tested = new SetDefaultStatementProducer()

    @Unroll
    def "should return statement '#expectedStatement' for table '#table' and column '#column' and default value '#defaultValue'" () {
        expect:
            tested.produce(table, column, defaultValue) == expectedStatement

        where:
            table       |   column      |   defaultValue                                    ||  expectedStatement
            "users"     |   "tenant_id" |   "current_setting('posmulten.current_tenant')"   ||  "ALTER TABLE users ALTER COLUMN tenant_id SET DEFAULT current_setting('posmulten.current_tenant');"
            "groups"    |   "tenant_id" |   "'value'"                                       ||  "ALTER TABLE groups ALTER COLUMN tenant_id SET DEFAULT 'value';"
            "users"     |   "col1"      |   "current_setting('posmulten.current_tenant')"   ||  "ALTER TABLE users ALTER COLUMN col1 SET DEFAULT current_setting('posmulten.current_tenant');"
            "groups"    |   "col1"      |   "'xxx1'"                                        ||  "ALTER TABLE groups ALTER COLUMN col1 SET DEFAULT 'xxx1';"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is null, no matter if column name \"#column\" or default value \"#defaultValue\" is correct"()
    {
        when:
            tested.produce(null, column, defaultValue)

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
            tested.produce(table, column, defaultValue)

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
            tested.produce(table, null, defaultValue)

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
            tested.produce(table, column, defaultValue)

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
            tested.produce(table, column, null)

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
            tested.produce(table, column, defaultValue)

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
