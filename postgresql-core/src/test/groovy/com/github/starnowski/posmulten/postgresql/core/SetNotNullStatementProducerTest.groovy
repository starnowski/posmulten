package com.github.starnowski.posmulten.postgresql.core

import spock.lang.Specification
import spock.lang.Unroll

class SetNotNullStatementProducerTest extends Specification {

    def tested = new SetNotNullStatementProducer()

    @Unroll
    def "should return statement '#expectedStatement' for table '#table' and column '#column'" () {
        expect:
            tested.produce(table, column) == expectedStatement

        where:
            table       |   column      ||  expectedStatement
            "users"     |   "tenant_id" ||  "ALTER TABLE users ALTER COLUMN tenant_id SET NOT NULL;"
            "groups"    |   "tenant_id" ||  "ALTER TABLE groups ALTER COLUMN tenant_id SET NOT NULL;"
            "users"     |   "col1" ||  "ALTER TABLE users ALTER COLUMN col1 SET NOT NULL;"
            "groups"    |   "col1" ||  "ALTER TABLE groups ALTER COLUMN col1 SET NOT NULL;"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when table name is null, no matter if column name is correct \"#column\""()
    {
        when:
            tested.produce(null, column)

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
            tested.produce(table, column)

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
            tested.produce(table, null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Column name cannot be null"

        where:
            table << ["xxx", "col1", "test"]
    }
}
