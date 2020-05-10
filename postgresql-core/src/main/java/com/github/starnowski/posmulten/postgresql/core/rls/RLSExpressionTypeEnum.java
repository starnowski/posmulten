package com.github.starnowski.posmulten.postgresql.core.rls;

public enum RLSExpressionTypeEnum implements RLSExpressionTypeSupplier{
    USING,
    WITH_CHECK;

    @Override
    public String getRLSExpressionTypeString() {
        return name();
    }
}
