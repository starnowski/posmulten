package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.ITableColumns;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.rls.IsTenantIdentifierValidConstraintProducer;

import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsTenantIdentifierValidConstraintProducerParameters.builder;

public class IsTenantIdentifierValidConstraintEnricher implements ISharedSchemaContextEnricher {

    private final IsTenantIdentifierValidConstraintProducer producer;

    public IsTenantIdentifierValidConstraintEnricher() {
        this(new IsTenantIdentifierValidConstraintProducer());
    }

    public IsTenantIdentifierValidConstraintEnricher(IsTenantIdentifierValidConstraintProducer producer) {
        this.producer = producer;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        if (request.isConstraintForValidTenantValueShouldBeAdded())
        {
            String defaultConstraintName = request.getIsTenantValidConstraintName() == null ? "tenant_identifier_valid" : request.getIsTenantValidConstraintName();
            for (Map.Entry<TableKey, ITableColumns> entry : request.getTableColumnsList().entrySet())
            {
                String constraintName = request.getTenantValidConstraintCustomNamePerTables().getOrDefault(entry.getKey(), defaultConstraintName);
                String tenantColumnName = entry.getValue().getTenantColumnName() == null ? request.getDefaultTenantIdColumn() : entry.getValue().getTenantColumnName();
                context.addSQLDefinition(producer.produce(builder()
                        .withConstraintName(constraintName)
                        .withTableName(entry.getKey().getTable())
                        .withTableSchema(entry.getKey().getSchema())
                        .withIIsTenantValidFunctionInvocationFactory(context.getIIsTenantValidFunctionInvocationFactory())
                        .withTenantColumnName(tenantColumnName).build()));
            }
        }
        return context;
    }
}
