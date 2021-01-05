package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy;
import com.github.starnowski.posmulten.configuration.core.model.TableEntry;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class RLSPolicyConfigurationEnricher implements ITableEntryEnricher {
    @Override
    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, TableEntry tableEntry) {
        if (tableEntry != null && tableEntry.getRlsPolicy() != null) {
            RLSPolicy rlsPolicy = tableEntry.getRlsPolicy();
            builder.createRLSPolicyForTable(tableEntry.getName(), rlsPolicy.getPrimaryKeyColumnsNameToTypeMap(), rlsPolicy.getTenantColumn(), rlsPolicy.getName());
        }
        return builder;
    }
}
