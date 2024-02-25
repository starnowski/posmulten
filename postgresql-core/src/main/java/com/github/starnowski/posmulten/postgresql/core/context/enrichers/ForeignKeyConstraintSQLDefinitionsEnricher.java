package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.DefaultForeignKeyConstraintStatementParameters;
import com.github.starnowski.posmulten.postgresql.core.ForeignKeyConstraintStatementProducer;
import com.github.starnowski.posmulten.postgresql.core.IForeignKeyConstraintStatementParameters;
import com.github.starnowski.posmulten.postgresql.core.context.ISameTenantConstraintForForeignKeyProperties;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SameTenantConstraintForForeignKey;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingConstraintNameDeclarationForTableException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.util.Pair;

import java.util.List;

import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ForeignKeyConstraintSQLDefinitionsEnricher implements ISharedSchemaContextEnricher {

    private final ForeignKeyConstraintStatementProducer producer;

    public ForeignKeyConstraintSQLDefinitionsEnricher() {
        this(new ForeignKeyConstraintStatementProducer());
    }

    public ForeignKeyConstraintSQLDefinitionsEnricher(ForeignKeyConstraintStatementProducer producer) {
        this.producer = producer;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        if (!TRUE.equals(request.getCreateForeignKeyConstraintWithTenantColumn())) {
            return context;
        }
        List<Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties>> constrainsRequests = request.getSameTenantConstraintForForeignKeyProperties().entrySet().stream().map(entry -> new Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties>(entry.getKey(), entry.getValue())).collect(toList());
        for (Pair<SameTenantConstraintForForeignKey, ISameTenantConstraintForForeignKeyProperties> constraintRequest : constrainsRequests) {
            SameTenantConstraintForForeignKey key = constraintRequest.getKey();
            ISameTenantConstraintForForeignKeyProperties requestProperties = constraintRequest.getValue();
            if (requestProperties.getConstraintName() == null) {
                throw new MissingConstraintNameDeclarationForTableException(key.getMainTable(), key.getForeignKeyColumns(),
                        format("Missing constraint name that in table %1$s and schema %2$s checks  if the foreign key columns (%3$s) refers to records that belong to the same tenant",
                                key.getMainTable().getTable(),
                                key.getMainTable().getSchema(),
                                key.getForeignKeyColumns().stream().sorted().collect(joining(", "))));
            }
            IForeignKeyConstraintStatementParameters parameters = DefaultForeignKeyConstraintStatementParameters.builder()
                    .withConstraintName(requestProperties.getConstraintName())
                    .withTableName(key.getMainTable().getTable())
                    .withTableSchema(key.getMainTable().getSchema())
                    .withReferenceTableKey(key.getForeignKeyTable())
                    .withForeignKeyColumnMappings(requestProperties.getForeignKeyPrimaryKeyColumnsMappings())
                    .build();
            context.addSQLDefinition(producer.produce(parameters));
        }
        return context;
    }
}
