package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;

public class IsRecordBelongsToCurrentTenantProducer extends ExtendedAbstractFunctionFactory<AbstractIsRecordBelongsToCurrentTenantProducerParameters, DefaultFunctionDefinition> {
    @Override
    protected String prepareReturnType(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters) {
        return null;
    }

    @Override
    protected void enrichMetadataPhraseBuilder(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {

    }

    @Override
    protected String buildBody(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters) {
        return null;
    }

    @Override
    protected DefaultFunctionDefinition returnFunctionDefinition(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new DefaultFunctionDefinition(functionDefinition);
    }
}
