package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.AbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.IFunctionDefinition;

public class EqualsCurrentTenantIdentifierFunctionProducer extends AbstractFunctionFactory<IEqualsCurrentTenantIdentifierFunctionProducerParameters, DefaultFunctionDefinition> {
    @Override
    protected DefaultFunctionDefinition returnFunctionDefinition(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new DefaultFunctionDefinition(functionDefinition);
    }

    @Override
    protected String produceStatement(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters) {
        return null;
    }
}
