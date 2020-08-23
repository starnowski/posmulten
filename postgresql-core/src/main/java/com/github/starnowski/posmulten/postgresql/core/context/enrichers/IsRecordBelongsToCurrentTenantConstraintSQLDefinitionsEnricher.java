package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.*;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingConstraintNameDeclarationForTableException;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;
import javafx.util.Pair;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher implements AbstractSharedSchemaContextEnricher {

    private IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer isRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer = new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) throws MissingConstraintNameDeclarationForTableException {
        List<Pair<SameTenantConstraintForForeignKey, AbstractSameTenantConstraintForForeignKeyProperties>> constrainsRequests = request.getSameTenantConstraintForForeignKeyProperties().entrySet().stream().map(entry -> new Pair<SameTenantConstraintForForeignKey, AbstractSameTenantConstraintForForeignKeyProperties>(entry.getKey(), entry.getValue())).collect(toList());
        for (Pair<SameTenantConstraintForForeignKey, AbstractSameTenantConstraintForForeignKeyProperties> constraintRequest : constrainsRequests)
        {
            SameTenantConstraintForForeignKey key = constraintRequest.getKey();
            AbstractSameTenantConstraintForForeignKeyProperties requestProperties = constraintRequest.getValue();
            //TODO Throw exception when there is no sql definition 'IsRecordBelongsToCurrentTenantFunctionInvocationFactory'
            if (requestProperties.getConstraintName() == null)
            {
                throw new MissingConstraintNameDeclarationForTableException(key.getMainTable(), key.getForeignKeyColumns(),
                        format("Missing constraint name that in table %1$s and schema %2$s checks  if the foreign key columns (%3$s) refers to records that belong to the same tenant",
                                key.getMainTable().getTable(),
                                key.getMainTable().getSchema(),
                                key.getForeignKeyColumns().stream().sorted().collect(joining(", "))));
            }
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
