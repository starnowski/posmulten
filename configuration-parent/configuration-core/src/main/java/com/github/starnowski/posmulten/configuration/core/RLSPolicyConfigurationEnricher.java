package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy;
import com.github.starnowski.posmulten.configuration.core.model.TableEntry;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

public class RLSPolicyConfigurationEnricher implements ITableEntryEnricher {
    @Override
    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, TableEntry tableEntry) {
        if (tableEntry != null && tableEntry.getRlsPolicy() != null) {
            RLSPolicy rlsPolicy = tableEntry.getRlsPolicy();
            if (tableEntry.getSchema() == null) {
                builder.createRLSPolicyForTable(tableEntry.getName(), rlsPolicy.getPrimaryKeyDefinition() == null ? null : rlsPolicy.getPrimaryKeyDefinition().getPrimaryKeyColumnsNameToTypeMap(), rlsPolicy.getTenantColumn(), rlsPolicy.getName());
            } else {
                builder.createRLSPolicyForTable(new TableKey(tableEntry.getName(), tableEntry.getSchema().orElse(null)), rlsPolicy.getPrimaryKeyDefinition() == null ? null : rlsPolicy.getPrimaryKeyDefinition().getPrimaryKeyColumnsNameToTypeMap(), rlsPolicy.getTenantColumn(), rlsPolicy.getName());
            }
            if (Boolean.TRUE.equals(rlsPolicy.getCreateTenantColumnForTable())) {
                if (tableEntry.getSchema() == null) {
                    builder.createTenantColumnForTable(tableEntry.getName());
                } else {
                    builder.createTenantColumnForTable(new TableKey(tableEntry.getName(), tableEntry.getSchema().orElse(null)));
                }
            }
            if (rlsPolicy.getPrimaryKeyDefinition() != null && rlsPolicy.getPrimaryKeyDefinition().getNameForFunctionThatChecksIfRecordExistsInTable() != null) {
                if (tableEntry.getSchema() == null) {
                    builder.setNameForFunctionThatChecksIfRecordExistsInTable(tableEntry.getName(), rlsPolicy.getPrimaryKeyDefinition().getNameForFunctionThatChecksIfRecordExistsInTable());
                } else {
                    builder.setNameForFunctionThatChecksIfRecordExistsInTable(new TableKey(tableEntry.getName(), tableEntry.getSchema().orElse(null)), rlsPolicy.getPrimaryKeyDefinition().getNameForFunctionThatChecksIfRecordExistsInTable());
                }
            }
            if (rlsPolicy.getValidTenantValueConstraintName() != null) {
                if (tableEntry.getSchema() == null) {
                    builder.registerCustomValidTenantValueConstraintNameForTable(tableEntry.getName(), rlsPolicy.getValidTenantValueConstraintName());
                } else {
                    builder.registerCustomValidTenantValueConstraintNameForTable(new TableKey(tableEntry.getName(), tableEntry.getSchema().orElse(null)), rlsPolicy.getValidTenantValueConstraintName());
                }
            }
            if (Boolean.TRUE.equals(rlsPolicy.getSkipAddingOfTenantColumnDefaultValue())) {
                builder.skipAddingOfTenantColumnDefaultValueForTable(tableEntry.getName());
            }
        }
        return builder;
    }
}
