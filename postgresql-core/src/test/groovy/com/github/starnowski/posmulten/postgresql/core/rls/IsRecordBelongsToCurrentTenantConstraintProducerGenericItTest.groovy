package com.github.starnowski.posmulten.postgresql.core.rls

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory

class IsRecordBelongsToCurrentTenantConstraintProducerGenericItTest extends AbstractConstraintProducerGenericItTest<IsRecordBelongsToCurrentTenantConstraintProducerParameters, IsRecordBelongsToCurrentTenantConstraintProducer> {
    @Override
    protected IsRecordBelongsToCurrentTenantConstraintProducer returnTestedObject() {
        new IsRecordBelongsToCurrentTenantConstraintProducer()
    }

    @Override
    protected IsRecordBelongsToCurrentTenantConstraintProducerParameters returnCorrectParametersMockObject() {
        IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory =
                {
                    "Cast(current_setting('some.boolean.value') as boolean)"
                }
        IsRecordBelongsToCurrentTenantConstraintProducerParameters mock = Mock(IsRecordBelongsToCurrentTenantConstraintProducerParameters)
        mock.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() >> isRecordBelongsToCurrentTenantFunctionInvocationFactory
        mock.getPrimaryColumnsValuesMap() >> [xxx: FunctionArgumentValue.forString("N/A")]
        mock
    }
}
