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
