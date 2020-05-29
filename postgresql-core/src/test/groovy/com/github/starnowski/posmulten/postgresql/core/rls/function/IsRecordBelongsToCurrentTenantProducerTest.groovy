package com.github.starnowski.posmulten.postgresql.core.rls.function

import spock.lang.Specification
import spock.lang.Unroll

class IsRecordBelongsToCurrentTenantProducerTest extends Specification {

    def tested = new IsRecordBelongsToCurrentTenantProducer()

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema' which returns type '#testReturnType' which returns value for property '#testCurrentTenantIdProperty'" () {
        expect:
        tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testReturnType)).getCreateScript() == expectedStatement

        where:
        testSchema              |   testFunctionName            |   testCurrentTenantIdProperty     |   testReturnType      || expectedStatement
        null                    |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION get_current_tenant() RETURNS VARCHAR(255) AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
        "public"                |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION public.get_current_tenant() RETURNS VARCHAR(255) AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
        "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION non_public_schema.get_current_tenant() RETURNS VARCHAR(255) AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
    }
}
