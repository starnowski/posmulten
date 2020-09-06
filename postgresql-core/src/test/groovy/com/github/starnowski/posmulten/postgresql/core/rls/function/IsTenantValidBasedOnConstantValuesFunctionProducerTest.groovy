package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest

class IsTenantValidBasedOnConstantValuesFunctionProducerTest extends AbstractFunctionFactoryTest {
    @Override
    protected returnTestedObject() {
        new IsTenantValidBasedOnConstantValuesFunctionProducer()
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(IsTenantValidBasedOnConstantValuesFunctionProducerParameters, constructorArgs: ["is_tenant_valid",
                                                                                "public",
                                                                                new HashSet<>(Arrays.asList("bad_tenant", "tenant_1")),
                                                                                "VARCHAR(255)"])
    }
}
