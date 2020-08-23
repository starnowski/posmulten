package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingFunctionNameDeclarationForTableException;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private IsRecordBelongsToCurrentTenantFunctionDefinitionProducer isRecordBelongsToCurrentTenantFunctionDefinitionProducer = new IsRecordBelongsToCurrentTenantFunctionDefinitionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) throws MissingFunctionNameDeclarationForTableException {
        List<TableKey> tableRequiredFunction = request.getSameTenantConstraintForForeignKeyProperties().keySet().stream().map(constraintKey -> constraintKey.getForeignKeyTable()).distinct().collect(toList());
        for (TableKey tableKey : tableRequiredFunction)
        {
            //TODO Throw exception when no name was defined
            String functionName = request.getFunctionThatChecksIfRecordExistsInTableNames().get(tableKey);
            if (functionName == null)
            {
                throw new MissingFunctionNameDeclarationForTableException(tableKey, format("Missing function name that checks if record exists in table %1$s and schema %2$s", tableKey.getTable(), tableKey.getSchema()));
            }
            IsRecordBelongsToCurrentTenantFunctionDefinition functionDefinition = isRecordBelongsToCurrentTenantFunctionDefinitionProducer.produce(tableKey, request.getTableColumnsList().get(tableKey), context.getIGetCurrentTenantIdFunctionInvocationFactory(), functionName, request.getDefaultSchema());
            context.addSQLDefinition(functionDefinition);
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(tableKey, functionDefinition);
        }
        return context;
    }

    void setIsRecordBelongsToCurrentTenantFunctionDefinitionProducer(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer isRecordBelongsToCurrentTenantFunctionDefinitionProducer) {
        this.isRecordBelongsToCurrentTenantFunctionDefinitionProducer = isRecordBelongsToCurrentTenantFunctionDefinitionProducer;
    }
}
