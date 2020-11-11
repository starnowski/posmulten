package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer;
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducerParameters;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;
import javafx.util.Pair;

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
        List<Pair<TableKey, String>> tableColumnPairs = emptyList();
        if (request.isCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables())
        {
            tableColumnPairs = request.getTableColumnsList().entrySet().stream().filter(entry -> !request.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().contains(entry.getKey())).map(entry -> new Pair<>(entry.getKey(), entry.getValue().getTenantColumnName())).collect(toList());
        } else if (!request.getCreateTenantColumnTableLists().isEmpty()) {
            tableColumnPairs = request.getTableColumnsList().entrySet().stream().filter(entry -> request.getCreateTenantColumnTableLists().contains(entry.getKey())).filter(entry -> !request.getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped().contains(entry.getKey())).map(entry -> new Pair<>(entry.getKey(), entry.getValue().getTenantColumnName())).collect(toList());
        }
        if (!tableColumnPairs.isEmpty())
        {
            String defaultTenantColumnValue = context.getIGetCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation();
            tableColumnPairs.forEach(tableColumnPair -> context.addSQLDefinition(producer.produce(new SetDefaultStatementProducerParameters(tableColumnPair.getKey().getTable(), tableColumnPair.getValue(), defaultTenantColumnValue, tableColumnPair.getKey().getSchema()))));
        }
        return context;
    }
}
