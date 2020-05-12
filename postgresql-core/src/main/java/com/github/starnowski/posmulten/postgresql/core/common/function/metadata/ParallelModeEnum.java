package com.github.starnowski.posmulten.postgresql.core.common.function.metadata;

public enum ParallelModeEnum implements ParallelModeSupplier{
    UNSAFE,
    RESTRICTED,
    SAFE;

    public static String PHRASE_PREFIX = "https://www.postgresql.org/docs/9.6/sql-createfunction.htm";

    @Override
    public String getParallelModeString() {
        return PHRASE_PREFIX + name();
    }
}
