package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;

import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentBuilder.forType;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.ParallelModeEnum.SAFE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.IMMUTABLE;
import static java.util.Collections.singletonList;

/**
 * @since 0.2
 */
public class IsTenantValidBasedOnConstantValuesFunctionProducer extends ExtendedAbstractFunctionFactory<IIsTenantValidBasedOnConstantValuesFunctionProducerParameters, IsTenantValidBasedOnConstantValuesFunctionDefinition> {
    @Override
    protected String prepareReturnType(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        return "BOOLEAN";
    }

    @Override
    protected void enrichMetadataPhraseBuilder(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {
        metadataPhraseBuilder.withParallelModeSupplier(SAFE).withVolatilityCategorySupplier(IMMUTABLE);
    }

    @Override
    protected String buildBody(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        return null;
    }

    @Override
    protected IsTenantValidBasedOnConstantValuesFunctionDefinition returnFunctionDefinition(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new IsTenantValidBasedOnConstantValuesFunctionDefinition(functionDefinition);
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(IIsTenantValidBasedOnConstantValuesFunctionProducerParameters parameters) {
        return singletonList(forType(parameters.getArgumentType() == null ? "text" : parameters.getArgumentType()));
    }
}
