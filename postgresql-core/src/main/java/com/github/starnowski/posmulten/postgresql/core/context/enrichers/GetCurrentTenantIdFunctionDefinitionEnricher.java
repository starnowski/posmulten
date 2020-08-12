package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.rls.function.GetCurrentTenantIdFunctionProducer;

public class GetCurrentTenantIdFunctionDefinitionEnricher implements AbstractSharedSchemaContextEnricher {

    private GetCurrentTenantIdFunctionProducer getCurrentTenantIdFunctionProducer = new GetCurrentTenantIdFunctionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        return null;
    }

    void setGetCurrentTenantIdFunctionProducer(GetCurrentTenantIdFunctionProducer getCurrentTenantIdFunctionProducer) {
        this.getCurrentTenantIdFunctionProducer = getCurrentTenantIdFunctionProducer;
    }
}
