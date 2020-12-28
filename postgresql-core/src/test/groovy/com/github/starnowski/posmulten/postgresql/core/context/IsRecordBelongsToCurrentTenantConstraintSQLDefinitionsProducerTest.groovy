package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference
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
            IIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters parameters =
                    builder()
                            .withTableKey(tableKey)
                            .withConstraintName(constraintName)
                            .withForeignKeyPrimaryKeyMappings(foreignKeyPrimaryKeyMappings)
                            .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                            .build()

        when:
            def results = tested.produce(parameters)

        then:
            1 * isRecordBelongsToCurrentTenantConstraintProducer.produce(_) >> 
                    {par ->
                        capturedParameters = par[0]
                        sqlDefinition
                    }
            results == [sqlDefinition]

        and: "pass correct parameters to inner component"
            capturedParameters.getConstraintName() == constraintName
            capturedParameters.getPrimaryColumnsValuesMap() == expectedKeyColumnsPairsList
            capturedParameters.getTableSchema() == tableKey.getSchema()
            capturedParameters.getTableName() == tableKey.getTable()
            capturedParameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() == isRecordBelongsToCurrentTenantFunctionInvocationFactory

        where:
            tableKey                        |   foreignKeyPrimaryKeyMappings                                        |   constraintName      ||  expectedKeyColumnsPairsList
            tk("users", null)               |   mapBuilder().put("id", "uuid").build()                              |   "fk_constraint"     ||  [uuid:forReference("id")]
            tk("users", "public")           |   mapBuilder().put("key", "bigint").put("uuid", "uuid").build()       |   "fk_user_public"    ||  [bigint:forReference("key"), "uuid":forReference("uuid")]
            tk("posts", "some_schema")      |   mapBuilder().put("uuid", "bigint").build()                          |   "posts_fk_"         ||  [bigint:forReference("uuid")]
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
