package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum
import spock.lang.Shared
import spock.lang.Unroll

class TenantHasAuthoritiesFunctionProducerTest extends AbstractFunctionFactoryTest {

    def tested = new TenantHasAuthoritiesFunctionProducer()

    @Shared
    def firstEqualsCurrentTenantIdentifierFunctionInvocationFactory = { tenant ->
        "is_tenant_starts_with_abcd(" + (FunctionArgumentValueEnum.STRING.equals(tenant.getType()) ? ("'" + tenant.getValue() + "'") : tenant.getValue()) + ")"
    }
    @Shared
    def secondEqualsCurrentTenantIdentifierFunctionInvocationFactory = { tenant ->
        "matches_current_tenant(" + (FunctionArgumentValueEnum.STRING.equals(tenant.getType()) ? ("'" + tenant.getValue() + "'") : tenant.getValue()) + ")"
    }

    @Unroll
    def "should generate statement that creates function for parameters object '#parametersObject' and return expected statement '#expectedStatement'" () {
        expect:
            tested.produce(parametersObject).getCreateScript() == expectedStatement

        where:
            parametersObject <<     [
                                        //1
                                        builder().withFunctionName("tenant_has_authorities").withSchema(null)
                                            .withEqualsCurrentTenantIdentifierFunctionInvocationFactory(firstEqualsCurrentTenantIdentifierFunctionInvocationFactory)
                                            .withTenantIdArgumentType("VARCHAR(312)")
                                            .withPermissionCommandPolicyArgumentType("text")
                                            .withRlsExpressionArgumentType("VARCHAR(73)")
                                            .withTableArgumentType("text")
                                            .withSchemaArgumentType("VARCHAR(117)")
                                            .build(),
                                        //2
                                        builder().withFunctionName("this_tenant_has_authorities").withSchema("secondary_schema")
                                             .withEqualsCurrentTenantIdentifierFunctionInvocationFactory(secondEqualsCurrentTenantIdentifierFunctionInvocationFactory)
                                             .withTenantIdArgumentType("VARCHAR(55)")
                                             .withPermissionCommandPolicyArgumentType("VARCHAR(534)")
                                             .withRlsExpressionArgumentType("text")
                                             .withTableArgumentType("VARCHAR(231)")
                                             .withSchemaArgumentType("text")
                                             .build()
                                    ]
            expectedStatement <<    [
                                        //1
                                        "CREATE OR REPLACE FUNCTION tenant_has_authorities(VARCHAR(312), text, VARCHAR(73), text, VARCHAR(117)) RETURNS BOOLEAN AS \$\$" +
                                        "\nSELECT is_tenant_starts_with_abcd(\$1)" +
                                        "\n\$\$ LANGUAGE sql" +
                                        "\nSTABLE" +
                                        "\nPARALLEL SAFE;",
                                        //2
                                        "CREATE OR REPLACE FUNCTION secondary_schema.this_tenant_has_authorities(VARCHAR(55), VARCHAR(534), text, VARCHAR(231), text) RETURNS BOOLEAN AS \$\$" +
                                                "\nSELECT matches_current_tenant(\$1)" +
                                                "\n\$\$ LANGUAGE sql" +
                                                "\nSTABLE" +
                                                "\nPARALLEL SAFE;"
                                    ]
    }

    private TenantHasAuthoritiesFunctionProducerParameters.TenantHasAuthoritiesFunctionProducerParametersBuilder builder()
    {
        new TenantHasAuthoritiesFunctionProducerParameters.TenantHasAuthoritiesFunctionProducerParametersBuilder()
    }

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(TenantHasAuthoritiesFunctionProducerParameters, constructorArgs: ["tenant_has_authorities", "public", firstEqualsCurrentTenantIdentifierFunctionInvocationFactory, "text", "VARCHAR(13)", "VARCHAR(512)", "VARCHAR(128)", "VARCHAR(32)"])
    }
}
