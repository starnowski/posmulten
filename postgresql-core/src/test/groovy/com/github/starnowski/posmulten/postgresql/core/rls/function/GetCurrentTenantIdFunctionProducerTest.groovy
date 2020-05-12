package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest
import spock.lang.Unroll

class GetCurrentTenantIdFunctionProducerTest extends AbstractFunctionFactoryTest {

    private static String VALID_CURRENT_TENANT_ID_PROPERTY_NAME = "c.c_ten"

    def tested = new GetCurrentTenantIdFunctionProducer()

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema' which returns type '#testReturnType' which returns value for property '#testCurrentTenantIdProperty'" () {
        expect:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, testCurrentTenantIdProperty, testSchema, testReturnType)).getCreateScript() == expectedStatement

        where:
            testSchema              |   testFunctionName            |   testCurrentTenantIdProperty     |   testReturnType      || expectedStatement
            null                    |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION get_current_tenant() RETURNS VARCHAR(255) AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION public.get_current_tenant() RETURNS VARCHAR(255) AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   null                ||  "CREATE OR REPLACE FUNCTION non_public_schema.get_current_tenant() RETURNS VARCHAR(255) AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            null                    |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION get_current_tenant() RETURNS text AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION public.get_current_tenant() RETURNS text AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "non_public_schema"     |   "get_current_tenant"        |   "c.c_ten"                       |   "text"              ||  "CREATE OR REPLACE FUNCTION non_public_schema.get_current_tenant() RETURNS text AS \$\$\nSELECT current_setting('c.c_ten')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            null                    |   "cur_tenant_val"            |   "con.tenant_id"                 |   "VARCHAR(128)"      ||  "CREATE OR REPLACE FUNCTION cur_tenant_val() RETURNS VARCHAR(128) AS \$\$\nSELECT current_setting('con.tenant_id')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "public"                |   "give_me_tenant"            |   "pos.tenant"                    |   "VARCHAR(32)"       ||  "CREATE OR REPLACE FUNCTION public.give_me_tenant() RETURNS VARCHAR(32) AS \$\$\nSELECT current_setting('pos.tenant')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
            "non_public_schema"     |   "return_current_tenant"     |   "t.id"                          |   "text"              ||  "CREATE OR REPLACE FUNCTION non_public_schema.return_current_tenant() RETURNS text AS \$\$\nSELECT current_setting('t.id')\n\$\$ LANGUAGE sql\nSTABLE\nPARALLEL SAFE;"
    }

    @Unroll
    def "should generate statement that returns the current tenant id function invocation '#expectedStatement' for schema #testSchema with name #testFunctionName" () {
        expect:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(testFunctionName, VALID_CURRENT_TENANT_ID_PROPERTY_NAME, testSchema, null)).returnGetCurrentTenantIdFunctionInvocation() == expectedStatement

        where:
            testSchema              |   testFunctionName            || expectedStatement
            null                    |   "get_current_tenant"        || "get_current_tenant()"
            "public"                |   "get_current_tenant"        || "public.get_current_tenant()"
            "non_public_schema"     |   "get_current_tenant"        || "non_public_schema.get_current_tenant()"
            null                    |   "return_current_tenant"     || "return_current_tenant()"
            "public"                |   "return_current_tenant"     || "public.return_current_tenant()"
            "non_public_schema"     |   "return_current_tenant"     || "non_public_schema.return_current_tenant()"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when tenant id property name is null, even if the rest of parameters are correct, function name #functionName, schema #testSchema, return type #testReturnType"()
    {
        when:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(functionName, null, testSchema, testReturnType))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Tenant id property name cannot be null"

        where:
            testSchema              |   functionName                |   testReturnType
            null                    |   "return_current_tenant"     |   null
            "public"                |   "return_current_tenant"     |   null
            "non_public_schema"     |   "return_current_tenant"     |   null
            null                    |   "return_current_tenant"     |   "text"
            "public"                |   "return_current_tenant"     |   "text"
            "non_public_schema"     |   "return_current_tenant"     |   "text"
            null                    |   "get_current_tenant"        |   "VARCHAR(128)"
            "public"                |   "get_current_tenant"        |   "VARCHAR(32)"
            "non_public_schema"     |   "get_current_tenant"        |   "text"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when tenant id property name is blank ('#testCurrentTenantIdProperty'), even if the rest of parameters are correct, function name #functionName, schema #testSchema, return type #testReturnType"()
    {
        when:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(functionName, testCurrentTenantIdProperty, testSchema, testReturnType))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Tenant id property name cannot be blank"

        where:
            testSchema              |   functionName                |   testReturnType  | testCurrentTenantIdProperty
            null                    |   "return_current_tenant"     |   null            | ""
            "public"                |   "return_current_tenant"     |   null            | "     "
            "non_public_schema"     |   "return_current_tenant"     |   null            | " "
            null                    |   "return_current_tenant"     |   "text"          | "         "
            "public"                |   "return_current_tenant"     |   "text"          | " "
            "non_public_schema"     |   "return_current_tenant"     |   "text"          | "             "
            null                    |   "get_current_tenant"        |   "VARCHAR(128)"  | " "
            "public"                |   "get_current_tenant"        |   "VARCHAR(32)"   | ""
            "non_public_schema"     |   "get_current_tenant"        |   "text"          | "          "
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when the return type is blank ('#testReturnType'), even if the rest of parameters are correct, function name #functionName, tenant id property name #testCurrentTenantIdProperty, schema name #testSchema"()
    {
        when:
            tested.produce(new GetCurrentTenantIdFunctionProducerParameters(functionName, testCurrentTenantIdProperty, testSchema, testReturnType))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Return type cannot be blank"

        where:
            testCurrentTenantIdProperty     |   functionName                |   testSchema          | testReturnType
            "c.c_ten"                       |   "return_current_tenant"     |   null                | ""
            "pos.tenant"                    |   "return_current_tenant"     |   null                | "     "
            "t.id"                          |   "return_current_tenant"     |   null                | " "
            "c.c_ten"                       |   "return_current_tenant"     |   "public"            | "         "
            "pos.tenant"                    |   "return_current_tenant"     |   "public"            | " "
            "t.id"                          |   "return_current_tenant"     |   "public"            | "             "
            "c.c_ten"                       |   "get_current_tenant"        |   "non_public_schema" | " "
            "pos.tenant"                    |   "get_current_tenant"        |   "non_public_schema" | ""
            "t.id"                          |   "get_current_tenant"        |   "non_public_schema" | "          "
    }

    def "should return an empty array of the arguments object"()
    {
        given:
            def parameters = new GetCurrentTenantIdFunctionProducerParameters("get_current_tenant", "c.c_ten", null, null)

        when:
            def result = tested.produce(parameters)

        then:
            result.getFunctionArguments() != null
            result.getFunctionArguments().isEmpty()
    }

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(GetCurrentTenantIdFunctionProducerParameters, constructorArgs: ["get_current_tenant_id", "conf.tenant_id", "public", "text"])
    }
}
