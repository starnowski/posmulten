package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.metadata.MetadataPhraseBuilder;

import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.ParallelModeEnum.SAFE;
import static com.github.starnowski.posmulten.postgresql.core.common.function.metadata.VolatilityCategoryEnum.STABLE;

/**
 * The component produces a statement that creates a function that returns the current tenant identifier.
 * For more details about function creation please check postgres documentation
 * @see <a href="https://www.postgresql.org/docs/9.6/sql-createfunction.html">Postgres, create function</a>
 *
 */
public class GetCurrentTenantIdFunctionProducer extends ExtendedAbstractFunctionFactory<IGetCurrentTenantIdFunctionProducerParameters, GetCurrentTenantIdFunctionDefinition> {

    protected void validate(IGetCurrentTenantIdFunctionProducerParameters parameters) {
        super.validate(parameters);
        if (parameters.getCurrentTenantIdProperty() == null)
        {
            throw new IllegalArgumentException("Tenant id property name cannot be null");
        }
        if (parameters.getCurrentTenantIdProperty().trim().isEmpty())
        {
            throw new IllegalArgumentException("Tenant id property name cannot be blank");
        }
        if (parameters.getFunctionReturnType() != null && parameters.getFunctionReturnType().trim().isEmpty())
        {
            throw new IllegalArgumentException("Return type cannot be blank");
        }
    }

    @Override
    protected GetCurrentTenantIdFunctionDefinition returnFunctionDefinition(IGetCurrentTenantIdFunctionProducerParameters parameters, IFunctionDefinition functionDefinition) {
        return new GetCurrentTenantIdFunctionDefinition(functionDefinition);
    }

    @Override
    protected String prepareReturnType(IGetCurrentTenantIdFunctionProducerParameters parameters) {
        return parameters.getFunctionReturnType() == null ? "VARCHAR(255)" : parameters.getFunctionReturnType();
    }

    @Override
    protected void enrichMetadataPhraseBuilder(IGetCurrentTenantIdFunctionProducerParameters parameters, MetadataPhraseBuilder metadataPhraseBuilder) {
        metadataPhraseBuilder.withParallelModeSupplier(SAFE).withVolatilityCategorySupplier(STABLE);
    }

    @Override
    protected String buildBody(IGetCurrentTenantIdFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT current_setting('");
        sb.append(parameters.getCurrentTenantIdProperty());
        sb.append("')");
        return sb.toString();
    }
}
