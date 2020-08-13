package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.context.enrichers.GetCurrentTenantIdFunctionDefinitionEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.SetCurrentTenantIdFunctionDefinitionEnricher;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class DefaultSharedSchemaContextBuilder {

    private List<AbstractSharedSchemaContextEnricher> enrichers = asList(new GetCurrentTenantIdFunctionDefinitionEnricher(), new SetCurrentTenantIdFunctionDefinitionEnricher());

    private SharedSchemaContextRequest sharedSchemaContextRequest = new SharedSchemaContextRequest();

    public AbstractSharedSchemaContext build()
    {
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

    public SharedSchemaContextRequest getSharedSchemaContextRequest() {
        return sharedSchemaContextRequest;
    }

    public void setCurrentTenantIdPropertyType(String currentTenantIdPropertyType) {
        sharedSchemaContextRequest.setCurrentTenantIdPropertyType(currentTenantIdPropertyType);
    }

    public void setCurrentTenantIdProperty(String currentTenantIdProperty) {
        sharedSchemaContextRequest.setCurrentTenantIdProperty(currentTenantIdProperty);
    }

    public void setDefaultSchema(String defaultSchema) {
        sharedSchemaContextRequest.setDefaultSchema(defaultSchema);
    }

    public void setGetCurrentTenantIdFunctionName(String getCurrentTenantIdFunctionName) {
        sharedSchemaContextRequest.setGetCurrentTenantIdFunctionName(getCurrentTenantIdFunctionName);
    }

    public void setSetCurrentTenantIdFunctionName(String setCurrentTenantIdFunctionName) {
        sharedSchemaContextRequest.setSetCurrentTenantIdFunctionName(setCurrentTenantIdFunctionName);
    }
}
