package com.github.starnowski.posmulten.postgresql.core.rls


import spock.lang.Specification
import spock.lang.Unroll

class GetCurrentTenantIdFunctionProducerTest extends Specification {

    def tested = new GetCurrentTenantIdFunctionProducer()

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema' which returns type '#testReturnType' which returns value for property '#testCurrentTenantIdProperty'" () {
        expect:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testReturnType)) == expectedStatement

        where:
            testSchema              |   testFunctionName            |   testCurrentTenantIdProperty     |   testReturnType      || expectedStatement
            null                    |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION get_current_tenant() RETURNS VARCHAR(255) as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "public"                |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION public.get_current_tenant() RETURNS VARCHAR(255) as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION non_public_schema.get_current_tenant() RETURNS VARCHAR(255) as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            null                    |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION get_current_tenant() RETURNS text as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "public"                |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION public.get_current_tenant() RETURNS text as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION non_public_schema.get_current_tenant() RETURNS text as \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            null                    |   "cur_tenant_val"            |   "con.tenant_id"                 |   "VARCHAR(128)"      ||  "CREATE OR REPLACE FUNCTION cur_tenant_val() RETURNS VARCHAR(128) as \$\$\nSELECT current_setting('con.tenant_id')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "public"                |   "give_me_tenant"            |   "pos.tenant"                    |   "VARCHAR(32)"       ||  "CREATE OR REPLACE FUNCTION public.give_me_tenant() RETURNS VARCHAR(32) as \$\$\nSELECT current_setting('pos.tenant')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "non_public_schema"     |   "return_current_tenant"     |   "t.id"                          |   "text"              ||  "CREATE OR REPLACE FUNCTION non_public_schema.return_current_tenant() RETURNS text as \$\$\nSELECT current_setting('t.id')\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
    }

    def "should throw exception of type 'IllegalArgumentException' when parameters object is null" ()
    {
        when:
            tested.produce(null)

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "The parameters object cannot be null"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when function name is null, even if the rest of parameters are correct, schema #schema, tenant id property #currentTenantIdProperty, return type #returnType"()
    {
        when:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(null, testCurrentTenantIdProperty, testSchema, testReturnType))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Function name cannot be null"

        where:
            testSchema              |   testCurrentTenantIdProperty     |   testReturnType
            null                    |   "c.c_ten"                       |   null
            "public"                |   "c.c_ten"                       |   null
            "non_public_schema"     |   "c.c_ten"                       |   null
            null                    |   "c.c_ten"                       |   "text"
            "public"                |   "c.c_ten"                       |   "text"
            "non_public_schema"     |   "c.c_ten"                       |   "text"
            null                    |   "con.tenant_id"                 |   "VARCHAR(128)"
            "public"                |   "pos.tenant"                    |   "VARCHAR(32)"
            "non_public_schema"     |   "t.id"                          |   "text"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when function name is blank ('#testFunctionName'), even if the rest of parameters are correct, schema #schema, tenant id property #currentTenantIdProperty, return type #returnType"()
    {
        when:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testReturnType))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Function name cannot be blank"

        where:
            testSchema              |   testCurrentTenantIdProperty     |   testReturnType  |   testFunctionName
            null                    |   "c.c_ten"                       |   null            |   ""
            "public"                |   "c.c_ten"                       |   null            |   "      "
            "non_public_schema"     |   "c.c_ten"                       |   null            |   "  "
            null                    |   "c.c_ten"                       |   "text"          |   "            "
            "public"                |   "c.c_ten"                       |   "text"          |   " "
            "non_public_schema"     |   "c.c_ten"                       |   "text"          |   ""
            null                    |   "con.tenant_id"                 |   "VARCHAR(128)"  |   "      "
            "public"                |   "pos.tenant"                    |   "VARCHAR(32)"   |   " "
            "non_public_schema"     |   "t.id"                          |   "text"          |   "     "
    }
}
