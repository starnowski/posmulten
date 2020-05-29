package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;
import javafx.util.Pair;

import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.ParallelModeEnum.SAFE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.STABLE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public class IsRecordBelongsToCurrentTenantProducer extends ExtendedAbstractFunctionFactory<AbstractIsRecordBelongsToCurrentTenantProducerParameters, DefaultFunctionDefinition> {

    public static final String RECORD_TABLE_ALIAS = "rt";

    @Override
    protected String prepareReturnType(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters) {
        return "BOOLEAN";
    }

    @Override
    protected void enrichMetadataPhraseBuilder(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {
        metadataPhraseBuilder.withParallelModeSupplier(SAFE).withVolatilityCategorySupplier(STABLE);
    }

    @Override
    protected String buildBody(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT EXISTS (");
        sb.append("\n");
        sb.append("\t");
        sb.append("SELECT 1 FROM ");
        sb.append(parameters.getRecordTableName());
        sb.append(" ");
        sb.append(RECORD_TABLE_ALIAS);
        sb.append(" ");
        sb.append("WHERE");
        sb.append(" ");
        //TODO
        sb.append("\n");
        sb.append(")");
        return sb.toString();
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
