package com.github.starnowski.posmulten.postgresql.core.context

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.MapBuilder.mapBuilder

class IsRecordBelongsToCurrentTenantFunctionDefinitionProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionProducer()

    @Unroll
    def "should generate sql definition that creates function which determines if record for specified table (#tableKey) exists"()
    {
        //new DefaultTableColumns("ten_ant_id", mapBuilder().put("id", "uuid").build())
        where:
            tableKey                        |   tenantId                |   idColumns                                                           |   functionName        |   schema
            tk("users", null)               |   "ten_ant_id"            |   mapBuilder().put("id", "uuid").build()                              |   "is_u_exists"       |   null
            tk("users", "public")           |   "tenant"                |   mapBuilder().put("key", "bigint").put("uuid", "uuid").build()       |   "is_users_exists"   |   null
            tk("posts", "some_schema")      |   "value_of_tenant"       |   mapBuilder().put("uuid", "bigint").build()                          |   "posts_exists"      |   "some_different_schema"
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
