package com.github.starnowski.posmulten.postgresql.core.rls.function


import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryTest

class EqualsCurrentTenantIdentifierFunctionProducerTest extends AbstractFunctionFactoryTest {

    def tested = new EqualsCurrentTenantIdentifierFunctionProducer()

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(EqualsCurrentTenantIdentifierFunctionProducerParameters, constructorArgs: ["is_current_tenant", "public", "VARCHAR(32)", { "get_tenant()"} ])
    }
}
