package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.TableEntry;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class ForeignKeyConfigurationsEnricher implements ITableEntryEnricher {

    private final ForeignKeyConfigurationEnricher foreignKeyConfigurationEnricher;

    public ForeignKeyConfigurationsEnricher() {
        this(new ForeignKeyConfigurationEnricher());
    }

    public ForeignKeyConfigurationsEnricher(ForeignKeyConfigurationEnricher foreignKeyConfigurationEnricher) {
        this.foreignKeyConfigurationEnricher = foreignKeyConfigurationEnricher;
    }

    @Override
    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, TableEntry tableEntry) {
        tableEntry.getForeignKeys().forEach(foreignKeyConfiguration -> foreignKeyConfigurationEnricher.enrich(builder, tableEntry.getName(), foreignKeyConfiguration));
        return builder;
    }
}
