package com.github.starnowski.posmulten.postgresql.core.rls;

/**
 * The component produces a statement that creates a function that returns the current tenant identifier.
 * For more details about function creation please check postgres documentation
 * @see <a href="https://www.postgresql.org/docs/9.6/sql-createfunction.html">Postgres, create function</a>
 *
 */
public class GetCurrentTenantIdFunctionProducer {

    public String produce(IGetCurrentTenantIdFunctionProducerParameters parameters)
    {
        validate(parameters);
        //TODO validation
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE OR REPLACE FUNCTION ");
        if (parameters.getSchema() != null)
        {
            sb.append(parameters.getSchema());
            sb.append(".");
        }
        sb.append(parameters.getFunctionName());
        sb.append("()");
        sb.append(" RETURNS ");
        if (parameters.getFunctionReturnType() == null)
        {
            sb.append("VARCHAR(255)");
        }
        else
        {
            sb.append(parameters.getFunctionReturnType());
        }
        sb.append(" as $$");
        sb.append("\n");
        sb.append("SELECT current_setting('");
        sb.append(parameters.getCurrentTenantIdProperty());
        sb.append("')");
        sb.append("\n");
        sb.append("$$ LANGUAGE sql");
        sb.append("\n");
        sb.append("STABLE PARALLEL SAFE");
        sb.append(";");
        return sb.toString();
    }

    private void validate(IGetCurrentTenantIdFunctionProducerParameters parameters) {
        if (parameters == null)
        {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        if (parameters.getFunctionName() == null)
        {
            throw new IllegalArgumentException("Function name cannot be null");
        }
        if (parameters.getFunctionName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Function name cannot be blank");
        }
        if (parameters.getCurrentTenantIdProperty() == null)
        {
            throw new IllegalArgumentException("Tenant id property name cannot be null");
        }
        if (parameters.getCurrentTenantIdProperty().trim().isEmpty())
        {
            throw new IllegalArgumentException("Tenant id property name cannot be blank");
        }
        if (parameters.getSchema() != null && parameters.getSchema().trim().isEmpty())
        {
            throw new IllegalArgumentException("Schema name cannot be blank");
        }
    }
}
