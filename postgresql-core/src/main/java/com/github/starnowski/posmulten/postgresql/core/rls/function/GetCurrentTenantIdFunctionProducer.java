package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.ExtendedAbstractFunctionFactory;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

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

//    @Override
//    protected String produceStatement(IGetCurrentTenantIdFunctionProducerParameters parameters) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("CREATE OR REPLACE FUNCTION ");
//        if (parameters.getSchema() != null)
//        {
//            sb.append(parameters.getSchema());
//            sb.append(".");
//        }
//        sb.append(parameters.getFunctionName());
//        sb.append("()");
//        sb.append(" RETURNS ");
//        if (parameters.getFunctionReturnType() == null)
//        {
//            sb.append("VARCHAR(255)");
//        }
//        else
//        {
//            sb.append(parameters.getFunctionReturnType());
//        }
//        sb.append(" as $$");
//        sb.append("\n");
//        sb.append("SELECT current_setting('");
//        sb.append(parameters.getCurrentTenantIdProperty());
//        sb.append("')");
//        sb.append("\n");
//        sb.append("$$ LANGUAGE sql");
//        sb.append("\n");
//        sb.append("STABLE PARALLEL SAFE");
//        sb.append(";");
//        return sb.toString();
//    }

    @Override
    protected String prepareReturnType(IGetCurrentTenantIdFunctionProducerParameters parameters) {
        return parameters.getFunctionReturnType() == null ? "VARCHAR(255)" : parameters.getFunctionReturnType();
    }

    @Override
    protected String buildBodyAndMetaData(IGetCurrentTenantIdFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT current_setting('");
        sb.append(parameters.getCurrentTenantIdProperty());
        sb.append("')");
        sb.append("\n");
        sb.append("$$ LANGUAGE sql");
        sb.append("\n");
        sb.append("STABLE PARALLEL SAFE");
        return sb.toString();
    }
}
