package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionFactoryParameters;

import java.util.Set;

/**
 * @since 0.2
 */
public interface IIsTenantValidBasedOnConstantValuesFunctionProducerParameters extends IFunctionFactoryParameters {

    String getArgumentType();

    Set<String> getBlacklistTenantIds();
}
