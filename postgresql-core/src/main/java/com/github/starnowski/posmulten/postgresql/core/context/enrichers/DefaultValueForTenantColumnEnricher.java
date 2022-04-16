package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer;
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class DefaultValueForTenantColumnEnricher implements ISharedSchemaContextEnricher {

    private final SetDefaultStatementProducer producer;

    public DefaultValueForTenantColumnEnricher() {
        this(new SetDefaultStatementProducer());
    }

    public DefaultValueForTenantColumnEnricher(SetDefaultStatementProducer producer) {
        this.producer = producer;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) {
        List<TableKey> tableKeys = emptyList();
        if (request.isCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables()) {
            tableKeys = request.getTableColumnsList().entrySet().stream().filter(entry -> !request.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().contains(entry.getKey())).map(entry -> entry.getKey()).collect(toList());
        } else if (!request.getCreateTenantColumnTableLists().isEmpty()) {
            tableKeys = request.getTableColumnsList().entrySet().stream().filter(entry -> request.getCreateTenantColumnTableLists().contains(entry.getKey())).filter(entry -> !request.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().contains(entry.getKey())).map(entry -> entry.getKey()).collect(toList());
        }
        if (!tableKeys.isEmpty()) {
            String defaultTenantColumnValue = context.getIGetCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation();
            tableKeys.forEach(tableKey -> context.addSQLDefinition(producer.produce(new SetDefaultStatementProducerParameters(tableKey.getTable(), request.resolveTenantColumnByTableKey(tableKey), defaultTenantColumnValue, tableKey.getSchema()))));
        }
        return context;
    }
}
