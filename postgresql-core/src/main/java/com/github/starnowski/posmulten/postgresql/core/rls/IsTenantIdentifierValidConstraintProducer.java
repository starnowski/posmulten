package com.github.starnowski.posmulten.postgresql.core.rls;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;

/**
 * @since 0.2
 */
public class IsTenantIdentifierValidConstraintProducer extends AbstractConstraintProducer<IIsTenantIdentifierValidConstraintProducerParameters> {
    @Override
    protected String prepareConstraintBody(IIsTenantIdentifierValidConstraintProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(parameters.getTenantColumnName());
        sb.append(" IS NULL OR ");
        sb.append(parameters.getIIsTenantValidFunctionInvocationFactory().returnIsTenantValidFunctionInvocation(forReference(parameters.getTenantColumnName())));
        return sb.toString();
    }

    protected void validate(IIsTenantIdentifierValidConstraintProducerParameters parameters)
    {
        super.validate(parameters);
        if (parameters.getIIsTenantValidFunctionInvocationFactory() == null)
        {
            throw new IllegalArgumentException("Object of type IIsTenantValidFunctionInvocationFactory cannot be null");
        }
        if (parameters.getTenantColumnName() == null)
        {
            throw new IllegalArgumentException("Tenant column cannot be null");
        }
        if (parameters.getTenantColumnName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Tenant column cannot be empty");
        }
    }
}
