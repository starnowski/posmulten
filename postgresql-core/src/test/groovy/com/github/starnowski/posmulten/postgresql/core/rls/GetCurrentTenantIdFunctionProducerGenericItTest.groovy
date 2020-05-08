package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.AbstractFunctionFactoryGenericItTest

class GetCurrentTenantIdFunctionProducerGenericItTest extends AbstractFunctionFactoryGenericItTest{

    def tested = new GetCurrentTenantIdFunctionProducer()

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        Spy(GetCurrentTenantIdFunctionProducerParameters, constructorArgs: ["get_current_tenant_id", "conf.tenant_id", "public", "text"])
    }
}
