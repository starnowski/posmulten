package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionFactoryParameters;

import java.util.Set;

public interface IIsTenantValidBasedOnConstantValuesFunctionProducerParameters extends IFunctionFactoryParameters {

    Set<String> getBlacklistTenantIds();
}
