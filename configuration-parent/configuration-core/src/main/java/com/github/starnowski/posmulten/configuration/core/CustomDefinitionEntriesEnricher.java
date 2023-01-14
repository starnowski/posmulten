package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

import java.util.List;
import java.util.Optional;

public class CustomDefinitionEntriesEnricher {

    private final CustomDefinitionEntryEnricher customDefinitionEntryEnricher;

    public CustomDefinitionEntriesEnricher() {
        this(new CustomDefinitionEntryEnricher());
    }

    public CustomDefinitionEntriesEnricher(CustomDefinitionEntryEnricher customDefinitionEntryEnricher) {
        this.customDefinitionEntryEnricher = customDefinitionEntryEnricher;
    }

    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, List<CustomDefinitionEntry> customDefinitionEntries) {
        Optional.ofNullable(customDefinitionEntries).ifPresent(entries -> {
            if (!entries.isEmpty()) {
                entries.forEach(cd -> {
                    this.customDefinitionEntryEnricher.enrich(builder, cd);
                });
            }
        });
        return builder;
    }

    CustomDefinitionEntryEnricher getCustomDefinitionEntryEnricher() {
        return customDefinitionEntryEnricher;
    }
}
