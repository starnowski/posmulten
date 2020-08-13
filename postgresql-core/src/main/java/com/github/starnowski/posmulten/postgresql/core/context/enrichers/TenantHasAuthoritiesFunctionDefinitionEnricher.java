package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;

public class TenantHasAuthoritiesFunctionDefinitionEnricher implements AbstractSharedSchemaContextEnricher {

    private EqualsCurrentTenantIdentifierFunctionProducer equalsCurrentTenantIdentifierFunctionProducer = new EqualsCurrentTenantIdentifierFunctionProducer();
    private TenantHasAuthoritiesFunctionProducer tenantHasAuthoritiesFunctionProducer = new TenantHasAuthoritiesFunctionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        String equalsCurrentTenantIdentifierFunctionName = request.getEqualsCurrentTenantIdentifierFunctionName() == null ? "is_id_equals_current_tenant_id" : request.getEqualsCurrentTenantIdentifierFunctionName();
        String tenantHasAuthoritiesFunctionName = request.getTenantHasAuthoritiesFunctionName() == null ? "tenant_has_authorities" : request.getTenantHasAuthoritiesFunctionName();
        EqualsCurrentTenantIdentifierFunctionDefinition equalsCurrentTenantIdentifierFunctionDefinition = equalsCurrentTenantIdentifierFunctionProducer.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(equalsCurrentTenantIdentifierFunctionName, request.getDefaultSchema(), request.getCurrentTenantIdPropertyType(), context.getIGetCurrentTenantIdFunctionInvocationFactory()));
        TenantHasAuthoritiesFunctionDefinition tenantHasAuthoritiesFunctionDefinition = tenantHasAuthoritiesFunctionProducer.produce(new TenantHasAuthoritiesFunctionProducerParameters(tenantHasAuthoritiesFunctionName, request.getDefaultSchema(), equalsCurrentTenantIdentifierFunctionDefinition));
        context.addSQLDefinition(equalsCurrentTenantIdentifierFunctionDefinition);
        context.addSQLDefinition(tenantHasAuthoritiesFunctionDefinition);
        context.setTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionDefinition);
        return context;
    }

    void setEqualsCurrentTenantIdentifierFunctionProducer(EqualsCurrentTenantIdentifierFunctionProducer equalsCurrentTenantIdentifierFunctionProducer) {
        this.equalsCurrentTenantIdentifierFunctionProducer = equalsCurrentTenantIdentifierFunctionProducer;
    }

    void setTenantHasAuthoritiesFunctionProducer(TenantHasAuthoritiesFunctionProducer tenantHasAuthoritiesFunctionProducer) {
        this.tenantHasAuthoritiesFunctionProducer = tenantHasAuthoritiesFunctionProducer;
    }
}
