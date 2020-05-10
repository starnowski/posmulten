package com.github.starnowski.posmulten.postgresql.core.rls.function


import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest
import spock.lang.Unroll

class EqualsCurrentTenantIdentifierFunctionProducerTest extends AbstractFunctionFactoryTest {

    def tested = new EqualsCurrentTenantIdentifierFunctionProducer()

    @Unroll
    def "should generate statement that creates function '#testFunctionName' for schema '#testSchema' which requires argument with type #testArgumentType and use the #testGetCurrentTenantIdFunction function" () {
        expect:
            tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, testArgumentType, { testGetCurrentTenantIdFunction })).getCreateScript() == exptectedStatement

        where:
            testSchema              |   testFunctionName            |   testGetCurrentTenantIdFunction      |   testArgumentType    || exptectedStatement
            null                    |   "is_current_tenant"         |   "get_current_tenant()"              |   null                ||   "CREATE OR REPLACE FUNCTION is_current_tenant(VARCHAR(255)) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "public"                |   "is_current_tenant"         |   "get_current_tenant()"              |   null                ||   "CREATE OR REPLACE FUNCTION public.is_current_tenant(VARCHAR(255)) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "non_public_schema"     |   "is_current_tenant"         |   "get_current_tenant()"              |   null                ||   "CREATE OR REPLACE FUNCTION non_public_schema.is_current_tenant(VARCHAR(255)) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            null                    |   "is_current_tenant"         |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION is_current_tenant(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "public"                |   "is_current_tenant"         |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION public.is_current_tenant(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "non_public_schema"     |   "is_current_tenant"         |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION non_public_schema.is_current_tenant(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            null                    |   "equal_cur_ten"             |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "public"                |   "equal_cur_ten"             |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION public.equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "non_public_schema"     |   "equal_cur_ten"             |   "get_current_tenant()"              |   "text"              ||   "CREATE OR REPLACE FUNCTION non_public_schema.equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = get_current_tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            null                    |   "equal_cur_ten"             |   "tenant()"                          |   "text"              ||   "CREATE OR REPLACE FUNCTION equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "public"                |   "equal_cur_ten"             |   "tenant()"                          |   "text"              ||   "CREATE OR REPLACE FUNCTION public.equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
            "non_public_schema"     |   "equal_cur_ten"             |   "tenant()"                          |   "text"              ||   "CREATE OR REPLACE FUNCTION non_public_schema.equal_cur_ten(text) RETURNS BOOLEAN as \$\$\nSELECT \$1 = tenant()\n\$\$ LANGUAGE sql\nSTABLE PARALLEL SAFE;"
    }

    @Unroll
    def "should throw exception of type 'IllegalArgumentException' when argument type IGetCurrentTenantIdFunctionInvocationFactory is null, even if the rest of parameters are correct, function name #functionName, schema #testSchema, return type #testArgumentType"()
    {
        when:
            tested.produce(new EqualsCurrentTenantIdentifierFunctionProducerParameters(testFunctionName, testSchema, testArgumentType, null))

        then:
            def ex = thrown(IllegalArgumentException.class)

        and: "exception should have correct message"
            ex.message == "Parameter of type IGetCurrentTenantIdFunctionInvocationFactory cannot be null"

        where:
            testSchema              |   testFunctionName            |   testArgumentType
            null                    |   "is_current_tenant"         |   null
            "public"                |   "is_current_tenant"         |   null
            "non_public_schema"     |   "is_current_tenant"         |   null
            null                    |   "is_current_tenant"         |   "text"
            "public"                |   "is_current_tenant"         |   "text"
            "non_public_schema"     |   "is_current_tenant"         |   "text"
            null                    |   "give_me_tenant"            |   "text"
            "public"                |   "give_me_tenant"            |   "text"
            "non_public_schema"     |   "give_me_tenant"            |   "text"
            null                    |   "give_me_tenant"            |   "VARCHAR(255)"
            "public"                |   "give_me_tenant"            |   "VARCHAR(255)"
            "non_public_schema"     |   "give_me_tenant"            |   "VARCHAR(255)"
    }

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(EqualsCurrentTenantIdentifierFunctionProducerParameters, constructorArgs: ["is_current_tenant", "public", "VARCHAR(32)", { "get_tenant()"} ])
    }
}
