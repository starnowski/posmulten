package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;

public class CurrentTenantIdPropertyTypeEnricher implements ISharedSchemaContextEnricher {
    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) {
        context.setCurrentTenantIdPropertyType(request.getCurrentTenantIdPropertyType());
        return context;
    }
}
