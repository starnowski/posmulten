package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.TableEntry;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public interface ITableEntryEnricher {

    DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, TableEntry tableEntry);

}