package com.github.starnowski.posmulten.postgresql.core.common.function.metadata;

public enum ParallelModeEnum implements ParallelModeSupplier{
    UNSAFE,
    RESTRICTED,
    SAFE;

    @Override
    public String getParallelModeString() {
        return name();
    }
}
