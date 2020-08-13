package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.rls.function.SetCurrentTenantIdFunctionProducer;

public class SetCurrentTenantIdFunctionDefinitionEnricher implements AbstractSharedSchemaContextEnricher {

    private SetCurrentTenantIdFunctionProducer setCurrentTenantIdFunctionProducer;

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        return null;
    }

    void setSetCurrentTenantIdFunctionProducer(SetCurrentTenantIdFunctionProducer setCurrentTenantIdFunctionProducer) {
        this.setCurrentTenantIdFunctionProducer = setCurrentTenantIdFunctionProducer;
    }
}
