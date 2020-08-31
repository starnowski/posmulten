package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsRecordBelongsToCurrentTenantProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantProducer
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.MapBuilder.mapBuilder
import static com.github.starnowski.posmulten.postgresql.core.rls.function.IIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType

class IsRecordBelongsToCurrentTenantFunctionDefinitionProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantFunctionDefinitionProducer()

    @Unroll
    def "should generate sql definition that creates function with name #functionName in schema #schema which determines if record for specified table (#tableKey) exists, for tenant column #tenantId and id columns #idColumns"()
    {
        given:
            def isRecordBelongsToCurrentTenantProducer = Mock(IsRecordBelongsToCurrentTenantProducer)
            tested.setIsRecordBelongsToCurrentTenantProducer(isRecordBelongsToCurrentTenantProducer)
            def tableColumns = new DefaultTableColumns(tenantId, idColumns)
            def iGetCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            def expectedSQLFunctionDefinition = Mock(IsRecordBelongsToCurrentTenantFunctionDefinition)
            IIsRecordBelongsToCurrentTenantProducerParameters capturedParameters = null;

        when:
            def result = tested.produce(tableKey, tableColumns, iGetCurrentTenantIdFunctionInvocationFactory, functionName, schema)

        then:
            1 * isRecordBelongsToCurrentTenantProducer.produce(_) >>
                    {   parameters ->
                        capturedParameters = parameters[0]
                        expectedSQLFunctionDefinition
                    }
        and: "pass correct parameters"
            capturedParameters.getSchema() == schema
            capturedParameters.getIGetCurrentTenantIdFunctionInvocationFactory() == iGetCurrentTenantIdFunctionInvocationFactory
            capturedParameters.getFunctionName() == functionName
            capturedParameters.getTenantColumn() == tenantId
            capturedParameters.getRecordSchemaName() == tableKey.getSchema()
            capturedParameters.getRecordTableName() == tableKey.getTable()
            capturedParameters.getKeyColumnsPairsList().containsAll(expectedKeyColumnsPairsList)
            capturedParameters.getKeyColumnsPairsList().size() == expectedKeyColumnsPairsList.size()

        where:
            tableKey                        |   tenantId                |   idColumns                                                           |   functionName        |   schema                  ||  expectedKeyColumnsPairsList
            tk("users", null)               |   "ten_ant_id"            |   mapBuilder().put("id", "uuid").build()                              |   "is_u_exists"       |   null                    ||  [pairOfColumnWithType("id", "uuid")]
            tk("users", "public")           |   "tenant"                |   mapBuilder().put("key", "bigint").put("uuid", "uuid").build()       |   "is_users_exists"   |   null                    ||  [pairOfColumnWithType("key", "bigint"), pairOfColumnWithType("uuid", "uuid")]
            tk("posts", "some_schema")      |   "value_of_tenant"       |   mapBuilder().put("uuid", "bigint").build()                          |   "posts_exists"      |   "some_different_schema" ||  [pairOfColumnWithType("uuid", "bigint")]
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
