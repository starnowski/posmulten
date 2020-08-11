package com.github.starnowski.posmulten.postgresql.core.context;

public interface AbstractSharedSchemaContextEnricher {

    AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request);
}
