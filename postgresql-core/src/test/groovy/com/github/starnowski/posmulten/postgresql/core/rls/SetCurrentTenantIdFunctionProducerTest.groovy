package com.github.starnowski.posmulten.postgresql.core.rls

class SetCurrentTenantIdFunctionProducerTest extends AbstractFunctionFactoryTest {

    def tested = new SetCurrentTenantIdFunctionProducer()

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(SetCurrentTenantIdFunctionProducerParameters, constructorArgs: ["set_current_tenant", "c.c_ten" , "public", "text"])
    }
}
