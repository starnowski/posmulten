package com.github.starnowski.posmulten.postgresql.core.context;

public enum CustomSQLDefinitionPairDefaultPosition implements CustomSQLDefinitionPairPositionProvider {
    AT_BEGINNING,
    AT_END;

    @Override
    public String getPosition() {
        return name();
    }
}
