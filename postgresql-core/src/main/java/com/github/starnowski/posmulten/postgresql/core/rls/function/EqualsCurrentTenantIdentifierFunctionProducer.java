package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

import java.util.List;

public class EqualsCurrentTenantIdentifierFunctionProducer extends AbstractFunctionFactory<IEqualsCurrentTenantIdentifierFunctionProducerParameters, DefaultFunctionDefinition> {
    @Override
    protected DefaultFunctionDefinition returnFunctionDefinition(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new DefaultFunctionDefinition(functionDefinition);
    }

    @Override
    protected String produceStatement(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters) {
        return null;
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(IEqualsCurrentTenantIdentifierFunctionProducerParameters parameters) {
        return null;
    }
}
