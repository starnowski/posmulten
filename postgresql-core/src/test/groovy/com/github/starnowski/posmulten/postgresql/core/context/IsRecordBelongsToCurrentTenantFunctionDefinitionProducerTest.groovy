package com.github.starnowski.posmulten.postgresql.core.context

import spock.lang.Specification
import spock.lang.Unroll

class IsRecordBelongsToCurrentTenantFunctionDefinitionProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionProducer()

    @Unroll
    def "should generate sql definition that creates function which determines if record for specified table (#tableKey) exists"()
    {

        where:
            tableKey    |   tableColumns    |   functionName
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
