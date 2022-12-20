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
