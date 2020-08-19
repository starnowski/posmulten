package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private IsRecordBelongsToCurrentTenantFunctionDefinitionProducer isRecordBelongsToCurrentTenantFunctionDefinitionProducer = new IsRecordBelongsToCurrentTenantFunctionDefinitionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        List<TableKey> tableRequiredFunction = request.getSameTenantConstraintForForeignKeyProperties().keySet().stream().map(constraintKey -> constraintKey.getForeignKeyTable()).collect(toList());
        for (TableKey tableKey : tableRequiredFunction)
        {
            //TODO Throw exception when no name was defined
            String functionName = request.getFunctionThatChecksIfRecordExistsInTableNames().get(tableKey);
            context.addSQLDefinition(isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(tableKey, request.getTableColumnsList().get(tableKey), context.getIGetCurrentTenantIdFunctionInvocationFactory(), functionName, request.getDefaultSchema() ));
        }
        return context;
    }

    void setIsRecordBelongsToCurrentTenantFunctionDefinitionProducer(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer isRecordBelongsToCurrentTenantFunctionDefinitionProducer) {
        this.isRecordBelongsToCurrentTenantFunctionDefinitionProducer = isRecordBelongsToCurrentTenantFunctionDefinitionProducer;
    }
}
