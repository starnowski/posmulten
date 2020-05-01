package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class CreateColumnStatementProducerTest extends Specification {

    def tested = new CreateColumnStatementProducer()

    @Unroll
    def "should return statement '#expectedStatement' for table '#table' and column '#column' and type '#columnType'" () {
        expect:
            tested.produce(new CreateColumnStatementProducerParameters(table, column, columnType, null)) == expectedStatement

        where:
            table       |   column      |   columnType                  ||  expectedStatement
            "users"     |   "tenant_id" |   "character varying(255)"    ||  "ALTER TABLE users ADD COLUMN tenant_id character varying(255);"
            "groups"    |   "tenant_id" |   "text"                      ||  "ALTER TABLE groups ADD COLUMN tenant_id text;"
            "users"     |   "col1"      |   "character varying(255)"    ||  "ALTER TABLE users ADD COLUMN col1 character varying(255);"
            "groups"    |   "col1"      |   "text"                      ||  "ALTER TABLE groups ADD COLUMN col1 text;"
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
}
