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

import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.Optional;

public class ForeignKeyConfigurationEnricher {

    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, String tableName, Optional<String> tableSchema, ForeignKeyConfiguration foreignKeyConfiguration) {
        if (tableSchema != null || foreignKeyConfiguration.getTableSchema() != null) {
            String defaultSchema = builder.getSharedSchemaContextRequestCopy().getDefaultSchema();
            return builder.createSameTenantConstraintForForeignKey(new TableKey(tableName, tableSchema == null ? defaultSchema : tableSchema.orElse(null)), new TableKey(foreignKeyConfiguration.getTableName(), foreignKeyConfiguration.getTableSchema() == null ? defaultSchema : foreignKeyConfiguration.getTableSchema().orElse(null)), foreignKeyConfiguration.getForeignKeyPrimaryKeyColumnsMappings(), foreignKeyConfiguration.getConstraintName());
        }
        return builder.createSameTenantConstraintForForeignKey(tableName, foreignKeyConfiguration.getTableName(), foreignKeyConfiguration.getForeignKeyPrimaryKeyColumnsMappings(), foreignKeyConfiguration.getConstraintName());
    }
}
