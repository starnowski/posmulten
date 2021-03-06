package com.github.starnowski.posmulten.postgresql.core.rls.function;

import java.util.Set;

/**
 * @since 0.2
 */
public class IsTenantValidBasedOnConstantValuesFunctionProducerParameters implements IIsTenantValidBasedOnConstantValuesFunctionProducerParameters{

    private final String functionName;
    private final String schema;
    private final Set<String> blacklistTenantIds;
    private final String argumentType;

    public IsTenantValidBasedOnConstantValuesFunctionProducerParameters(String functionName, String schema, Set<String> blacklistTenantIds, String argumentType) {
        this.functionName = functionName;
        this.schema = schema;
        this.blacklistTenantIds = blacklistTenantIds;
        this.argumentType = argumentType;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public Set<String> getBlacklistTenantIds() {
        return blacklistTenantIds;
    }

    @Override
    public String getArgumentType() {
        return argumentType;
    }
}
