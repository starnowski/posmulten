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
            "non_public_schema"     |   "set_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION non_public_schema.set_current_tenant(text) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('c.c_ten', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
            null                    |   "set_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION set_current_tenant(text) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('c.c_ten', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
            "public"                |   "set_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION public.set_current_tenant(text) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('c.c_ten', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
            "non_public_schema"     |   "set_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION non_public_schema.set_current_tenant(text) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('c.c_ten', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
            null                    |   "this_is_tenant"            |   "con.tenant_id"                 |   "VARCHAR(128)"      ||  "CREATE OR REPLACE FUNCTION this_is_tenant(VARCHAR(128)) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('con.tenant_id', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
            "public"                |   "this_is_tenant"            |   "pos.tenant"                    |   "VARCHAR(32)"       ||  "CREATE OR REPLACE FUNCTION public.this_is_tenant(VARCHAR(32)) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('pos.tenant', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
            "non_public_schema"     |   "this_is_tenant"            |   "t.id"                          |   "text"              ||  "CREATE OR REPLACE FUNCTION non_public_schema.this_is_tenant(text) RETURNS VOID AS \$\$\nBEGIN\nPERFORM set_config('t.id', \$1, false);\nEND\n\$\$ LANGUAGE plpgsql\nVOLATILE;"
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
