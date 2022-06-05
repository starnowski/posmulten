package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

/**
 * The pair of the custom SQL definition and its position.
 */
public class CustomSQLDefinitionPair {

    /**
     * Position of custom SQL definition
     */
    private final String position;

    /**
     * Custom SQL definition object {@link SQLDefinition}
     */
    private final SQLDefinition sqlDefinition;

    public CustomSQLDefinitionPair(String position, SQLDefinition sqlDefinition) {
        this.position = position;
        this.sqlDefinition = sqlDefinition;
    }

    public String getPosition() {
        return position;
    }

    public SQLDefinition getSqlDefinition() {
        return sqlDefinition;
    }
}
