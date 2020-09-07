package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryGenericItTest

class IsTenantValidBasedOnConstantValuesFunctionProducerGenericItTest extends AbstractFunctionFactoryGenericItTest{

    def tested = new IsTenantValidBasedOnConstantValuesFunctionProducer()

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(IsTenantValidBasedOnConstantValuesFunctionProducerParameters, constructorArgs: ["is_tenant_valid",
                                                                                            "public",
                                                                                            new HashSet<>(Arrays.asList("13", "2137")),
                                                                                            "INTEGER"])
    }
}
