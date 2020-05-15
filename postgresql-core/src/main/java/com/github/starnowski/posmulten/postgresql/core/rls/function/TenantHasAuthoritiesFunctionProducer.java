package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentBuilder;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;

import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.ParallelModeEnum.SAFE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.STABLE;

public class TenantHasAuthoritiesFunctionProducer extends ExtendedAbstractFunctionFactory<ITenantHasAuthoritiesFunctionProducerParameters, TenantHasAuthoritiesFunctionDefinition> {

    public static final String DEFAULT_ARGUMENT_TYPE = "VARCHAR(255)";

    @Override
    protected TenantHasAuthoritiesFunctionDefinition returnFunctionDefinition(ITenantHasAuthoritiesFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new TenantHasAuthoritiesFunctionDefinition(functionDefinition);
    }

    @Override
    protected List<IFunctionArgument> prepareFunctionArguments(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        return Arrays.asList(argument(parameters.getTenantIdArgumentType()), argument(parameters.getPermissionCommandPolicyArgumentType()), argument(parameters.getRLSExpressionArgumentType())
        ,argument(parameters.getTableArgumentType()), argument(parameters.getSchemaArgumentType()));
    }

    private IFunctionArgument argument(String type)
    {
        return FunctionArgumentBuilder.forType(type == null ? DEFAULT_ARGUMENT_TYPE : type);
    }

    @Override
    protected String prepareReturnType(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        return "BOOLEAN";
    }

    @Override
    protected void enrichMetadataPhraseBuilder(ITenantHasAuthoritiesFunctionProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {
        metadataPhraseBuilder.withVolatilityCategorySupplier(STABLE).withParallelModeSupplier(SAFE);
    }

    @Override
    protected void validate(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        super.validate(parameters);
        if (parameters.getEqualsCurrentTenantIdentifierFunctionInvocationFactory() == null)
        {
            throw new IllegalArgumentException("Parameter of type EqualsCurrentTenantIdentifierFunctionInvocationFactory cannot be null");
        }
    }

    @Override
    protected String buildBody(ITenantHasAuthoritiesFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(parameters.getEqualsCurrentTenantIdentifierFunctionInvocationFactory().returnEqualsCurrentTenantIdentifierFunctionInvocation(forReference("$1")));
        return sb.toString();
    }
}
