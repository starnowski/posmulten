package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.TableEntry;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class TablesEntriesEnricher {

    private final List<ITableEntryEnricher> enrichers;

    public TablesEntriesEnricher()
    {
        this(Arrays.asList(new RLSPolicyConfigurationEnricher(), new ForeignKeyConfigurationsEnricher()));
    }

    public TablesEntriesEnricher(List<ITableEntryEnricher> enrichers) {
        this.enrichers = unmodifiableList(enrichers);
    }

    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, List<TableEntry> tableEntries) {
        //TODO
        return null;
    }
}
