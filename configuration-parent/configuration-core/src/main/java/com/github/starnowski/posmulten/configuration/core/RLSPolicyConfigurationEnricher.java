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

            if (Boolean.TRUE.equals(rlsPolicy.getCreateTenantColumnForTable())) {
                builder.createTenantColumnForTable(tableEntry.getName());
            }
            if (rlsPolicy.getNameForFunctionThatChecksIfRecordExistsInTable() != null) {
                builder.setNameForFunctionThatChecksIfRecordExistsInTable(tableEntry.getName(), rlsPolicy.getNameForFunctionThatChecksIfRecordExistsInTable());
            }
            if (rlsPolicy.getValidTenantValueConstraintName() != null) {
                builder.registerCustomValidTenantValueConstraintNameForTable(tableEntry.getName(), rlsPolicy.getValidTenantValueConstraintName());
            }
        }
        return builder;
    }
}
