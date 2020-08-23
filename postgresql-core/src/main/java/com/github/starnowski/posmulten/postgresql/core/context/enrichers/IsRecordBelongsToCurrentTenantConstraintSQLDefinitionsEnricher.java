package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;
import javafx.util.Pair;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        List<Pair<SameTenantConstraintForForeignKey, AbstractSameTenantConstraintForForeignKeyProperties>> constrainsRequests = request.getSameTenantConstraintForForeignKeyProperties().entrySet().stream().map(entry -> new Pair<SameTenantConstraintForForeignKey, AbstractSameTenantConstraintForForeignKeyProperties>(entry.getKey(), entry.getValue())).collect(toList());
        for (Pair<SameTenantConstraintForForeignKey, AbstractSameTenantConstraintForForeignKeyProperties> constraintRequest : constrainsRequests)
        {
            SameTenantConstraintForForeignKey key = constraintRequest.getKey();
            AbstractSameTenantConstraintForForeignKeyProperties requestProperties = constraintRequest.getValue();
            //TODO Throw exception when no name was defined, for constraint and there is no sql definition 'IsRecordBelongsToCurrentTenantFunctionInvocationFactory'
            IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory = context.getTableKeysIsRecordBelongsToCurrentTenantFunctionInvocationFactoryMap().get(key.getForeignKeyTable());
            AbstractIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters parameters = IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters.builder()
                    .withConstraintName(requestProperties.getConstraintName())
                    .withTableKey(key.getMainTable())
                    .withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(isRecordBelongsToCurrentTenantFunctionInvocationFactory)
                    .withForeignKeyPrimaryKeyMappings(requestProperties.getForeignKeyPrimaryKeyColumnsMappings())
                    .build();
            isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer.produce(parameters).forEach(sqlDefinition -> context.addSQLDefinition(sqlDefinition));
        }
        return context;
    }

    void setIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer) {
        this.isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer;
    }
}
