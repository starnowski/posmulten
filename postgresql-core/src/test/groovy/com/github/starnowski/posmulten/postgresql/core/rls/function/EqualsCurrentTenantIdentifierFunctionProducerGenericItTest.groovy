package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryGenericItTest

class EqualsCurrentTenantIdentifierFunctionProducerGenericItTest extends AbstractFunctionFactoryGenericItTest{

    def tested = new EqualsCurrentTenantIdentifierFunctionProducer()

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(EqualsCurrentTenantIdentifierFunctionProducerParameters, constructorArgs: ["is_current_tenant", "public", "VARCHAR(32)", { "current_setting('no.such.property')"} ])
    }
}
