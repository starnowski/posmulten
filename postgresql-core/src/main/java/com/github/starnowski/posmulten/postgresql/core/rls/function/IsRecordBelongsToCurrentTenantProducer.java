package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;
import javafx.util.Pair;

import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.ParallelModeEnum.SAFE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.STABLE;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class IsRecordBelongsToCurrentTenantProducer extends ExtendedAbstractFunctionFactory<AbstractIsRecordBelongsToCurrentTenantProducerParameters, IsRecordBelongsToCurrentTenantFunctionDefinition> {

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
        if (parameters.getRecordSchemaName() != null)
        {
            sb.append(parameters.getRecordSchemaName());
            sb.append(".");
        }
        sb.append(parameters.getRecordTableName());
        sb.append(" ");
        sb.append(RECORD_TABLE_ALIAS);
        sb.append(" ");
        sb.append("WHERE");
        sb.append(" ");
        sb.append(
            range(0, parameters.getKeyColumnsPairsList().size())
                .mapToObj(i -> format("%1$s.%2$s = $%3$s", RECORD_TABLE_ALIAS, parameters.getKeyColumnsPairsList().get(i).getKey(), i + 1)).collect(joining(" AND "))
        );
        sb.append(" AND ");
        sb.append(format("%1$s = %2$s", RECORD_TABLE_ALIAS + "." + parameters.getTenantColumn(), parameters.getIGetCurrentTenantIdFunctionInvocationFactory().returnGetCurrentTenantIdFunctionInvocation()));
        sb.append("\n");
        sb.append(")");
        return sb.toString();
    }

    @Override
    protected IsRecordBelongsToCurrentTenantFunctionDefinition returnFunctionDefinition(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new IsRecordBelongsToCurrentTenantFunctionDefinition(functionDefinition, unmodifiableList(parameters.getKeyColumnsPairsList()));
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters) {
        return parameters.getKeyColumnsPairsList().stream().map(Pair::getValue).collect(toList());
    }

    @Override
    protected void validate(AbstractIsRecordBelongsToCurrentTenantProducerParameters parameters) {
        super.validate(parameters);
        if (parameters.getRecordSchemaName() != null && parameters.getRecordSchemaName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Record schema name cannot be blank");
        }
        if (parameters.getRecordTableName() == null)
        {
            throw new IllegalArgumentException("Record table name cannot be null");
        }
        if (parameters.getRecordTableName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Record table name cannot be blank");
        }
        if (parameters.getTenantColumn() == null)
        {
            throw new IllegalArgumentException("Tenant column cannot be null");
        }
        if (parameters.getTenantColumn().trim().isEmpty())
        {
            throw new IllegalArgumentException("Tenant column cannot be blank");
        }
        if (parameters.getIGetCurrentTenantIdFunctionInvocationFactory() == null)
        {
            throw new IllegalArgumentException("The GetCurrentTenantId function invocation factory cannot be null");
        }
        if (parameters.getKeyColumnsPairsList() == null)
        {
            throw new IllegalArgumentException("The list of primary key column pairs cannot be null");
        }
        if (parameters.getKeyColumnsPairsList().isEmpty())
        {
            throw new IllegalArgumentException("The list of primary key column pairs cannot be empty");
        }
        parameters.getKeyColumnsPairsList().stream().forEach(pair ->
        {
            if (pair == null)
            {
                throw new IllegalArgumentException("The list element of primary key column pairs cannot be null");
            }
         });
        parameters.getKeyColumnsPairsList().stream().forEach(pair ->
        {
            if (pair.getKey() == null)
            {
                throw new IllegalArgumentException("The list of primary key column pairs contains pair which key is null");
            }
            if (pair.getKey().trim().isEmpty())
            {
                throw new IllegalArgumentException("The list of primary key column pairs contains pair which key is blank");
            }
        });
    }
}
