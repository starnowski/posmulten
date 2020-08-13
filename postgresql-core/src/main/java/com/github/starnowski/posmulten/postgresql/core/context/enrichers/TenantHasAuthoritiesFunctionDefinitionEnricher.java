package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.rls.function.EqualsCurrentTenantIdentifierFunctionProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.function.TenantHasAuthoritiesFunctionProducer;

public class TenantHasAuthoritiesFunctionDefinitionEnricher implements AbstractSharedSchemaContextEnricher {

    private EqualsCurrentTenantIdentifierFunctionProducer equalsCurrentTenantIdentifierFunctionProducer = new EqualsCurrentTenantIdentifierFunctionProducer();
    private TenantHasAuthoritiesFunctionProducer tenantHasAuthoritiesFunctionProducer = new TenantHasAuthoritiesFunctionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        return null;
    }

    public void setEqualsCurrentTenantIdentifierFunctionProducer(EqualsCurrentTenantIdentifierFunctionProducer equalsCurrentTenantIdentifierFunctionProducer) {
        this.equalsCurrentTenantIdentifierFunctionProducer = equalsCurrentTenantIdentifierFunctionProducer;
    }

    public void setTenantHasAuthoritiesFunctionProducer(TenantHasAuthoritiesFunctionProducer tenantHasAuthoritiesFunctionProducer) {
        this.tenantHasAuthoritiesFunctionProducer = tenantHasAuthoritiesFunctionProducer;
    }
}
