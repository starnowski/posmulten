package com.github.starnowski.posmulten.postgresql.core.rls;

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
        /*
current_setting('poc.current_tenant')
         */

        return null;
    }
}
