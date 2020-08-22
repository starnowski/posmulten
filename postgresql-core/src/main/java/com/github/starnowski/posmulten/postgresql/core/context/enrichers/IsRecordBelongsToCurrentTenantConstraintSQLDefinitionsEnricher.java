package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinition;
import javafx.util.Pair;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        List<Pair<SameTenantConstraintForForeignKey, SameTenantConstraintForForeignKeyProperties>> constrainsRequests = request.getSameTenantConstraintForForeignKeyProperties().entrySet().stream().map(entry -> new Pair(entry.getKey(), entry.getValue())).collect(toList());
        for (Pair<SameTenantConstraintForForeignKey, SameTenantConstraintForForeignKeyProperties> constraintRequest : constrainsRequests)
        {
            //TODO Throw exception when no name was defined
            String functionName = request.getFunctionThatChecksIfRecordExistsInTableNames().get(tableKey);
            IsRecordBelongsToCurrentTenantFunctionDefinition functionDefinition = isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(tableKey, request.getTableColumnsList().get(tableKey), context.getIGetCurrentTenantIdFunctionInvocationFactory(), functionName, request.getDefaultSchema());
            context.addSQLDefinition(functionDefinition);
            context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().put(tableKey, functionDefinition);
        }
        return context;
    }

    void setIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer) {
        this.isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer;
    }
}
