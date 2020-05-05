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
        //TODO
        /*
        CREATE OR REPLACE FUNCTION pg_catalog.current_setting(
	text,
	boolean)
    RETURNS text
    LANGUAGE 'internal'

    COST 1
    STABLE STRICT PARALLEL SAFE
    ROWS 0
AS $BODY$
show_config_by_name_missing_ok
$BODY$;
         */
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
}
