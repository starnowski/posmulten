package com.github.starnowski.posmulten.postgresql.core.rls;

public class SetCurrentTenantIdFunctionProducer extends AbstractFunctionFactory<ISetCurrentTenantIdFunctionProducerParameters>{

    @Override
    protected void validate(ISetCurrentTenantIdFunctionProducerParameters parameters) {
        super.validate(parameters);
        if (parameters.getCurrentTenantIdProperty() == null)
        {
            throw new IllegalArgumentException("Tenant id property name cannot be null");
        }
        if (parameters.getCurrentTenantIdProperty().trim().isEmpty())
        {
            throw new IllegalArgumentException("Tenant id property name cannot be blank");
        }
        if (parameters.getArgumentType() != null && parameters.getArgumentType().trim().isEmpty())
        {
            throw new IllegalArgumentException("Argument type cannot be blank");
        }
    }

    @Override
    protected String produceStatement(ISetCurrentTenantIdFunctionProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE OR REPLACE FUNCTION ");
        if (parameters.getSchema() != null)
        {
            sb.append(parameters.getSchema());
            sb.append(".");
        }
        sb.append(parameters.getFunctionName());
        sb.append("(");
        if (parameters.getArgumentType() == null)
        {
            sb.append("text");
        }
        else
        {
            sb.append(parameters.getArgumentType());
        }
        sb.append(")");
        sb.append(" RETURNS ");
        sb.append("VOID");
        sb.append(" AS $$");
        sb.append("\n");
        sb.append("BEGIN");
        sb.append("\n");
        sb.append("PERFORM set_config('");
        sb.append(parameters.getCurrentTenantIdProperty());
        sb.append("', $1, false);");
        sb.append("\n");
        sb.append("END");
        sb.append("\n");
        sb.append("$$ LANGUAGE plpgsql");
        sb.append("\n");
        sb.append("VOLATILE");
        sb.append(";");
        return sb.toString();
    }
}
