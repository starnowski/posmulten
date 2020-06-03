package com.github.starnowski.posmulten.postgresql.core.rls.function

import com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactoryGenericItTest

import static com.github.starnowski.posmulten.postgresql.core.rls.function.AbstractIsRecordBelongsToCurrentTenantProducerParameters.pairOfColumnWithType

class IsRecordBelongsToCurrentTenantProducerGenericItTest extends AbstractFunctionFactoryGenericItTest{

    def tested = new IsRecordBelongsToCurrentTenantProducer()

    @Override
    protected returnTestedObject() {
        tested
    }

    @Override
    protected returnCorrectParametersSpyObject() {
        IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory =
                {
                    "current_setting('cur.tenant')"
                }
        Spy(IsRecordBelongsToCurrentTenantProducerParameters, constructorArgs: ["is_comment_belongs_to_current_tenant",
                                                                                "public",
                                                                                [pairOfColumnWithType("id", "int"), pairOfColumnWithType("user_id", "bigint")],
                                                                                "tenant",
                                                                                "comments",
                                                                                "non_public_schema",
                                                                                getCurrentTenantIdFunctionInvocationFactory])
    }
}
