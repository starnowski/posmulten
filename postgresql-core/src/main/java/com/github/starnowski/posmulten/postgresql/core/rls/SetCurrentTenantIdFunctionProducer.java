package com.github.starnowski.posmulten.postgresql.core.rls;

public class SetCurrentTenantIdFunctionProducer extends AbstractFunctionFactory<ISetCurrentTenantIdFunctionProducerParameters>{
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
        sb.append("void");
        sb.append(" as $$");
        sb.append("\n");
        sb.append("SELECT set_config('");
        sb.append(parameters.getCurrentTenantIdProperty());
        sb.append("', $1, false)");
        sb.append("\n");
        sb.append("$$ LANGUAGE sql");
        sb.append("\n");
        sb.append("VOLATILE");
        sb.append(";");
        return sb.toString();
    }
}
