package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.rls.function.*;

import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType;
import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantFunctionDefinitionProducer {

    private IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer = new IsRecordBelongsToCurrentTenantProducer();

    public IsRecordBelongsToCurrentTenantFunctionDefinition produce(TableKey tableKey, AbstractTableColumns tableColumns, IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory, String functionName, String schema)
    {
        AbstractIsRecordBelongsToCurrentTenantProducerParameters isRecordBelongsToCurrentTenantProducerParameters = new IsRecordBelongsToCurrentTenantProducerParameters.Builder()
                .withSchema(schema)
                .withFunctionName(functionName)
                .withRecordTableName(tableKey.getTable())
                .withRecordSchemaName(tableKey.getSchema())
                .withiGetCurrentTenantIdFunctionInvocationFactory(iGetCurrentTenantIdFunctionInvocationFactory)
                .withTenantColumn(tableColumns.getTenantColumnName())
                .withKeyColumnsPairsList(tableColumns.getIdentityColumnNameAndTypeMap().entrySet().stream().map(entry -> pairOfColumnWithType(entry.getKey(), entry.getValue())).collect(toList())).build();
        return isRecordBelongsToCurrentTenantProducer.produce(isRecordBelongsToCurrentTenantProducerParameters);
    }

    void setIsRecordBelongsToCurrentTenantProducer(IsRecordBelongsToCurrentTenantProducer isRecordBelongsToCurrentTenantProducer) {
        this.isRecordBelongsToCurrentTenantProducer = isRecordBelongsToCurrentTenantProducer;
    }
}
