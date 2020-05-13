package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum

class TenantHasAuthoritiesFunctionProducerTest extends AbstractFunctionFactoryTest {

    def tested = new TenantHasAuthoritiesFunctionProducer()

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        def equalsCurrentTenantIdentifierFunctionInvocationFactory = { tenant ->
            "is_tenant_starts_with_abcd(" + (FunctionArgumentValueEnum.STRING.equals(tenant.getType()) ? ("'" + tenant.getValue() + "'") : tenant.getValue()) + ")"
        }
        Spy(TenantHasAuthoritiesFunctionProducerParameters, constructorArgs: ["tenant_has_authorities", "public", equalsCurrentTenantIdentifierFunctionInvocationFactory, "text", "VARCHAR(13)", "VARCHAR(512)", "VARCHAR(128)", "VARCHAR(32)"])
    }
}
