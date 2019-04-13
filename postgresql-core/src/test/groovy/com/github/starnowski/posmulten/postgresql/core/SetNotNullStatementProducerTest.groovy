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
}
