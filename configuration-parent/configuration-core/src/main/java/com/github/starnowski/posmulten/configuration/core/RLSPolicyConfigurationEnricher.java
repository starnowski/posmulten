package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy;
import com.github.starnowski.posmulten.configuration.core.model.TableEntry;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class RLSPolicyConfigurationEnricher implements ITableEntryEnricher {
    @Override
    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, TableEntry tableEntry) {
        if (tableEntry != null && tableEntry.getRlsPolicy() != null) {
            RLSPolicy rlsPolicy = tableEntry.getRlsPolicy();
            builder.createRLSPolicyForTable(tableEntry.getName(), rlsPolicy.getPrimaryKeyDefinition() == null ? null : rlsPolicy.getPrimaryKeyDefinition().getPrimaryKeyColumnsNameToTypeMap(), rlsPolicy.getTenantColumn(), rlsPolicy.getName());

            if (Boolean.TRUE.equals(rlsPolicy.getCreateTenantColumnForTable())) {
                builder.createTenantColumnForTable(tableEntry.getName());
            }
            if (rlsPolicy.getPrimaryKeyDefinition() != null && rlsPolicy.getPrimaryKeyDefinition().getNameForFunctionThatChecksIfRecordExistsInTable() != null) {
                builder.setNameForFunctionThatChecksIfRecordExistsInTable(tableEntry.getName(), rlsPolicy.getPrimaryKeyDefinition().getNameForFunctionThatChecksIfRecordExistsInTable());
            }
            if (rlsPolicy.getValidTenantValueConstraintName() != null) {
                builder.registerCustomValidTenantValueConstraintNameForTable(tableEntry.getName(), rlsPolicy.getValidTenantValueConstraintName());
            }
            if (Boolean.TRUE.equals(rlsPolicy.getSkipAddingOfTenantColumnDefaultValue())) {
                builder.skipAddingOfTenantColumnDefaultValueForTable(tableEntry.getName());
            }
        }
        return builder;
    }
}
