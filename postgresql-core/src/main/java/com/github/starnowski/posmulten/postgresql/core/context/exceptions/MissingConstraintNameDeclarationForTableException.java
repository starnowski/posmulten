package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.Set;

public class MissingConstraintNameDeclarationForTableException extends SharedSchemaContextBuilderException{

    private final TableKey tableKey;
    private final Set<String> foreignKeysColumns;

    public MissingConstraintNameDeclarationForTableException(TableKey tableKey,  Set<String> foreignKeysColumns, String message) {
        super(message);
        this.tableKey = tableKey;
        this.foreignKeysColumns = foreignKeysColumns;
    }

    public Set<String> getForeignKeysColumns() {
        return foreignKeysColumns;
    }

    public TableKey getTableKey() {
        return tableKey;
    }
}
