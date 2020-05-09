package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryGenericItTest

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
