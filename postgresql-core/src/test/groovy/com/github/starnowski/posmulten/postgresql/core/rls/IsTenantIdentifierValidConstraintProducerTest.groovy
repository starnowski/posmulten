package com.github.starnowski.posmulten.postgresql.core.rls


import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidFunctionInvocationFactory

class IsTenantIdentifierValidConstraintProducerTest extends AbstractConstraintProducerTest<IIsTenantIdentifierValidConstraintProducerParameters, IsTenantIdentifierValidConstraintProducer> {
    @Override
    protected IsTenantIdentifierValidConstraintProducer returnTestedObject() {
        new IsTenantIdentifierValidConstraintProducer()
    }

    @Override
    protected IIsTenantIdentifierValidConstraintProducerParameters returnCorrectParametersMockObject() {
        IIsTenantValidFunctionInvocationFactory factory =
                { paramerts ->
                    "XXXXXX"
                }
        IIsTenantIdentifierValidConstraintProducerParameters mock = Mock(IIsTenantIdentifierValidConstraintProducerParameters)
        mock.getConstraintName() >> "const_1"
        mock.getTableName() >> "users"
        mock.getTableSchema() >> "public"
        mock.getIIsTenantValidFunctionInvocationFactory() >> factory
        mock.getTenantColumnName() >> "tenant"
        mock
    }
}
