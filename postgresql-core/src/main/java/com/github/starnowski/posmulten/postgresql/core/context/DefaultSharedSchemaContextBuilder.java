package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.ArrayList;
import java.util.List;

public class DefaultSharedSchemaContextBuilder {

    private List<AbstractSharedSchemaContextEnricher> enrichers;

    public AbstractSharedSchemaContext build()
    {
        //TODO
        return null;
    }

    public List<AbstractSharedSchemaContextEnricher> getEnrichers() {
        return enrichers == null ? new ArrayList<>() : new ArrayList<>(enrichers);
    }

    public void setEnrichers(List<AbstractSharedSchemaContextEnricher> enrichers) {
        this.enrichers = enrichers;
    }


}
