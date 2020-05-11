package com.github.starnowski.posmulten.postgresql.core.common.function.metadata;

public enum VolatilityCategoryEnum implements VolatilityCategorySupplier{
    IMMUTABLE,
    STABLE,
    VOLATILE;

    @Override
    public String getVolatilityCategoryString() {
        return name();
    }
}
