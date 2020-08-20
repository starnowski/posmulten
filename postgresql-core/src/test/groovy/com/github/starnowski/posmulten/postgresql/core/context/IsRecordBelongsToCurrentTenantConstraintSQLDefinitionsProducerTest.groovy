package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.MapBuilder.mapBuilder
import static com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder
import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType

class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer()

    @Unroll
    def "should create all required SQL definition that creates constraint for foreign key"()
    {
        given:
            def isRecordBelongsToCurrentTenantConstraintProducer = Mock(IsRecordBelongsToCurrentTenantConstraintProducer)
            tested.setIsRecordBelongsToCurrentTenantConstraintProducer(isRecordBelongsToCurrentTenantConstraintProducer)
            IsRecordBelongsToCurrentTenantConstraintProducerParameters capturedParameters = null
            def sqlDefinition = Mock(SQLDefinition)
            def isRecordBelongsToCurrentTenantFunctionInvocationFactory = Mock(IsRecordBelongsToCurrentTenantFunctionInvocationFactory)
            AbstractIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters parameters =
                    builder()
                            .withTableKey(tableKey)
                            .withConstraintName(constraintName)
                            .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                            .build()

        when:
            def results = tested.produce(tableKey, false)

        then:
            1 * isRecordBelongsToCurrentTenantConstraintProducer.produce(_) >> 
                    {par ->
                        capturedParameters = par[0]
                        sqlDefinition
                    }
            results == [sqlDefinition]

        where:
            tableKey                        |   tenantId                |   idColumns                                                           |   constraintName      ||  expectedKeyColumnsPairsList
            tk("users", null)               |   "ten_ant_id"            |   mapBuilder().put("id", "uuid").build()                              |   "is_u_exists"       ||  [pairOfColumnWithType("id", "uuid")]
            tk("users", "public")           |   "tenant"                |   mapBuilder().put("key", "bigint").put("uuid", "uuid").build()       |   "is_users_exists"   ||  [pairOfColumnWithType("key", "bigint"), pairOfColumnWithType("uuid", "uuid")]
            tk("posts", "some_schema")      |   "value_of_tenant"       |   mapBuilder().put("uuid", "bigint").build()                          |   "posts_exists"      ||  [pairOfColumnWithType("uuid", "bigint")]
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
