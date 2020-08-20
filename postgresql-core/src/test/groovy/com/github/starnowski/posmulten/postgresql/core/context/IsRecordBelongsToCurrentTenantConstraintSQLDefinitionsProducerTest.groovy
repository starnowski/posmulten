package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.core.context.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder 

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
                    builder().withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
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
            tableKey                        |   _
            tk("users", null)               |   _
            tk("users", "public")           |   _
            tk("users", "other_schema")     |   _
            tk("some_tab", "other_schema")  |   _
            tk("some_tab", "other_schema")  |   _
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
