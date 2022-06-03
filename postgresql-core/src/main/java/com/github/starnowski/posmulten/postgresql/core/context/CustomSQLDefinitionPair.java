package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

public class CustomSQLDefinitionPair {

    private final String position;
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
