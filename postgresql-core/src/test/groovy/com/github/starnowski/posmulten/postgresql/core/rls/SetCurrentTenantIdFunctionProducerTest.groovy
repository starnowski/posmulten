package com.github.starnowski.posmulten.postgresql.core.rls

import spock.lang.Unroll

class SetCurrentTenantIdFunctionProducerTest extends AbstractFunctionFactoryTest {

    def tested = new SetCurrentTenantIdFunctionProducer()

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema' with argument type '#testArgumentType' (null means 'text') which sets value for property '#testCurrentTenantIdProperty'" () {
        expect:
            tested.produce(new SetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testArgumentType)) == expectedStatement

        where:
            testSchema              |   testFunctionName            |   testCurrentTenantIdProperty     |   testArgumentType    || expectedStatement
            null                    |   "set_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION set_current_tenant(text) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('c.c_ten', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
            "public"                |   "set_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION public.set_current_tenant(text) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('c.c_ten', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
//            "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION non_public_schema.get_current_tenant() RETURNS VARCHAR(255) as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
//            null                    |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION get_current_tenant() RETURNS text as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
//            "public"                |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION public.get_current_tenant() RETURNS text as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
//            "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION non_public_schema.get_current_tenant() RETURNS text as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
//            null                    |   "cur_tenant_val"            |   "con.tenant_id"                 |   "VARCHAR(128)"      ||  "CREATE OR REPLACE FUNCTION cur_tenant_val() RETURNS VARCHAR(128) as \$\$\nSELECT current_setting('con.tenant_id')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
//            "public"                |   "give_me_tenant"            |   "pos.tenant"                    |   "VARCHAR(32)"       ||  "CREATE OR REPLACE FUNCTION public.give_me_tenant() RETURNS VARCHAR(32) as \$\$\nSELECT current_setting('pos.tenant')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
//            "non_public_schema"     |   "return_current_tenant"     |   "t.id"                          |   "text"              ||  "CREATE OR REPLACE FUNCTION non_public_schema.return_current_tenant() RETURNS text as \$\$\nSELECT current_setting('t.id')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
    }

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(SetCurrentTenantIdFunctionProducerParameters, constructorArgs: ["set_current_tenant", "c.c_ten" , "public", "text"])
    }
}
