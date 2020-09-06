package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;

/**
 * @since 0.2
 */
public class IsTenantValidBasedOnConstantValuesFunctionProducer extends ExtendedAbstractFunctionFactory<IIsTenantValidBasedOnConstantValuesFunctionProducerParameters, IsTenantValidBasedOnConstantValuesFunctionDefinition> {
    @Override
    protected String prepareReturnType(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        return null;
    }

    @Override
    protected void enrichMetadataPhraseBuilder(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {

    }

    @Override
    protected String buildBody(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        return null;
    }

    @Override
    protected IsTenantValidBasedOnConstantValuesFunctionDefinition returnFunctionDefinition(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new IsTenantValidBasedOnConstantValuesFunctionDefinition(functionDefinition);
    }
}
