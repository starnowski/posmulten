package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

public class TenantHasAuthoritiesFunctionProducer extends AbstractFunctionFactory<ITenantHasAuthoritiesFunctionProducerParameters, TenantHasAuthoritiesFunctionDefinition> {
    @Override
    protected TenantHasAuthoritiesFunctionDefinition returnFunctionDefinition(ITenantHasAuthoritiesFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new TenantHasAuthoritiesFunctionDefinition(functionDefinition);
    }

    @Override
    protected String produceStatement(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        return null;
    }
}
