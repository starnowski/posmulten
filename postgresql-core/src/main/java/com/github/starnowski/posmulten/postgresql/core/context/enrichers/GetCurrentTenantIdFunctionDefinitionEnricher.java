package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.rls.function.GetCurrentTenantIdFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.function.GetCurrentTenantIdFunctionProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.function.GetCurrentTenantIdFunctionProducerParameters;

public class GetCurrentTenantIdFunctionDefinitionEnricher implements AbstractSharedSchemaContextEnricher {

    private GetCurrentTenantIdFunctionProducer getCurrentTenantIdFunctionProducer = new GetCurrentTenantIdFunctionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        String functionName = request.getGetCurrentTenantIdFunctionName() == null ? "get_current_tenant_id" : request.getGetCurrentTenantIdFunctionName();
        GetCurrentTenantIdFunctionDefinition sqlDefinition = getCurrentTenantIdFunctionProducer.produce(new GetCurrentTenantIdFunctionProducerParameters(functionName, request.getCurrentTenantIdProperty(), request.getDefaultSchema(), request.getCurrentTenantIdPropertyType()));
        context.addSQLDefinition(sqlDefinition);
        context.setIGetCurrentTenantIdFunctionInvocationFactory(sqlDefinition);
        return context;
    }

    void setGetCurrentTenantIdFunctionProducer(GetCurrentTenantIdFunctionProducer getCurrentTenantIdFunctionProducer) {
        this.getCurrentTenantIdFunctionProducer = getCurrentTenantIdFunctionProducer;
    }
}
