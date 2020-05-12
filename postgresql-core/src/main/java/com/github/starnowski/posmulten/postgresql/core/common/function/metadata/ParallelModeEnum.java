package com.github.starnowski.posmulten.postgresql.core.common.function.metadata;

public enum ParallelModeEnum implements ParallelModeSupplier{
    UNSAFE,
    RESTRICTED,
    SAFE;

    public static String PHRASE_PREFIX = "PARALLEL ";

    @Override
    public String getParallelModeString() {
        return PHRASE_PREFIX + name();
    }
}
