package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.AbstractFunctionFactoryGenericItTest

class SetCurrentTenantIdFunctionProducerGenericItTest extends AbstractFunctionFactoryGenericItTest{

    def tested = new SetCurrentTenantIdFunctionProducer()

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(SetCurrentTenantIdFunctionProducerParameters, constructorArgs: ["set_current_tenant", "c.c_ten", "public", "text"])
    }
}
