package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.AbstractSharedSchemaContextEnricher;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.rls.function.*;

public class SetCurrentTenantIdFunctionDefinitionEnricher implements AbstractSharedSchemaContextEnricher {

    private SetCurrentTenantIdFunctionProducer setCurrentTenantIdFunctionProducer = new SetCurrentTenantIdFunctionProducer();

    @Override
    public AbstractSharedSchemaContext enrich(AbstractSharedSchemaContext context, SharedSchemaContextRequest request) {
        String functionName = request.getSetCurrentTenantIdFunctionName() == null ? "set_current_tenant_id" : request.getSetCurrentTenantIdFunctionName();
        SetCurrentTenantIdFunctionDefinition sqlDefinition = setCurrentTenantIdFunctionProducer.produce(new SetCurrentTenantIdFunctionProducerParameters(functionName, request.getCurrentTenantIdProperty(), request.getDefaultSchema(), request.getCurrentTenantIdPropertyType()));
        context.addSQLDefinition(sqlDefinition);
        context.setISetCurrentTenantIdFunctionInvocationFactory(sqlDefinition);
        return context;
    }

    void setSetCurrentTenantIdFunctionProducer(SetCurrentTenantIdFunctionProducer setCurrentTenantIdFunctionProducer) {
        this.setCurrentTenantIdFunctionProducer = setCurrentTenantIdFunctionProducer;
    }
}
