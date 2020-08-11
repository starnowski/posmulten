package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.ArrayList;
import java.util.List;

public class DefaultSharedSchemaContextBuilder {

    private List<AbstractSharedSchemaContextEnricher> enrichers;

    private SharedSchemaContextRequest sharedSchemaContextRequest = new SharedSchemaContextRequest();

    public AbstractSharedSchemaContext build()
    {
        //TODO
        AbstractSharedSchemaContext context = new SharedSchemaContext();
        List<AbstractSharedSchemaContextEnricher> enrichers  = getEnrichers();
        //TODO Consider of copy request
        for (AbstractSharedSchemaContextEnricher enricher : enrichers)
        {
            //TODO Consider of copy request (in loop also)
            context = enricher.enrich(context, sharedSchemaContextRequest);
        }
        return context;
    }

    public List<AbstractSharedSchemaContextEnricher> getEnrichers() {
        return enrichers == null ? new ArrayList<>() : new ArrayList<>(enrichers);
    }

    public void setEnrichers(List<AbstractSharedSchemaContextEnricher> enrichers) {
        this.enrichers = enrichers;
    }


}
