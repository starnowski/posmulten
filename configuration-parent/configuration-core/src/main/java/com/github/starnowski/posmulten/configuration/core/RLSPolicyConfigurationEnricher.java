/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
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
            } else {
                TableKey tableKey = new TableKey(tableEntry.getName(), tableEntry.getSchema().orElse(null));
                builder.createRLSPolicyForTable(tableKey, rlsPolicy.getPrimaryKeyDefinition() == null ? null : rlsPolicy.getPrimaryKeyDefinition().getPrimaryKeyColumnsNameToTypeMap(), rlsPolicy.getTenantColumn(), rlsPolicy.getName());
                if (Boolean.TRUE.equals(rlsPolicy.getCreateTenantColumnForTable())) {
                    builder.createTenantColumnForTable(tableKey);
                }
                if (rlsPolicy.getPrimaryKeyDefinition() != null && rlsPolicy.getPrimaryKeyDefinition().getNameForFunctionThatChecksIfRecordExistsInTable() != null) {
                    builder.setNameForFunctionThatChecksIfRecordExistsInTable(tableKey, rlsPolicy.getPrimaryKeyDefinition().getNameForFunctionThatChecksIfRecordExistsInTable());
                }
                if (rlsPolicy.getValidTenantValueConstraintName() != null) {
                    builder.registerCustomValidTenantValueConstraintNameForTable(tableKey, rlsPolicy.getValidTenantValueConstraintName());
                }
                if (Boolean.TRUE.equals(rlsPolicy.getSkipAddingOfTenantColumnDefaultValue())) {
                    builder.skipAddingOfTenantColumnDefaultValueForTable(tableKey);
                }
            }
        }
        return builder;
    }
}
