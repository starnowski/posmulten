package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class ForeignKeyConfigurationEnricher {

    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, String tableName, ForeignKeyConfiguration foreignKeyConfiguration) {
        return builder.createSameTenantConstraintForForeignKey(tableName, foreignKeyConfiguration.getTableName(), foreignKeyConfiguration.getForeignKeyPrimaryKeyColumnsMappings(), foreignKeyConfiguration.getConstraintName());
    }
}
