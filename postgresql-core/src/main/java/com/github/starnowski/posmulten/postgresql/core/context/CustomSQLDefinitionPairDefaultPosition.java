package com.github.starnowski.posmulten.postgresql.core.context;

/**
 * Default implementation of {@link CustomSQLDefinitionPairPositionProvider} type.
 */
public enum CustomSQLDefinitionPairDefaultPosition implements CustomSQLDefinitionPairPositionProvider {
    /**
     * Custom definition is being added before all definitions created by builder
     */
    AT_BEGINNING,
    /**
     * Custom definition is being added after all definitions created by builder
     */
    AT_END;

    @Override
    public String getPosition() {
        return name();
    }
}
