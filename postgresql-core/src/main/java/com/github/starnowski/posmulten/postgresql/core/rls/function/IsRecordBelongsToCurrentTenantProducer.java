package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;
import javafx.util.Pair;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public class IsRecordBelongsToCurrentTenantProducer extends ExtendedAbstractFunctionFactory<AbstractIsRecordBelongsToCurrentTenantProducerParameters, DefaultFunctionDefinition> {
    @Override
    protected String prepareReturnType(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters) {
        return "BOOLEAN";
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

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters) {
        return concat(parameters.getKeyColumnsPairsList().stream().map(Pair::getValue), of(parameters.getTenantColumnPair().getValue())).collect(toList());
    }
}
