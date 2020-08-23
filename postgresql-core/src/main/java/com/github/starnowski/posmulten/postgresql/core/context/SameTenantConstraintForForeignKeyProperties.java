package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.Map;

public class SameTenantConstraintForForeignKeyProperties implements AbstractSameTenantConstraintForForeignKeyProperties{

    private final String constraintName;
    private final Map<String, String> foreignKeyPrimaryKeyColumnsMappings;

    public SameTenantConstraintForForeignKeyProperties(String constraintName, Map<String, String> foreignKeyPrimaryKeyColumnsMappings) {
        this.constraintName = constraintName;
        this.foreignKeyPrimaryKeyColumnsMappings = foreignKeyPrimaryKeyColumnsMappings;
    }

    public Map<String, String> getForeignKeyPrimaryKeyColumnsMappings() {
        return foreignKeyPrimaryKeyColumnsMappings;
    }

    public String getConstraintName() {
        return constraintName;
    }
}
