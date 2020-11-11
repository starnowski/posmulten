package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidFunctionInvocationFactory;

/**
 * @since 0.2
 */
public interface IIsTenantIdentifierValidConstraintProducerParameters extends IConstraintProducerParameters {

    String getTenantColumnName();

    IIsTenantValidFunctionInvocationFactory getIIsTenantValidFunctionInvocationFactory();
}
