package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.Set;

public class IncorrectForeignKeysMappingException extends SharedSchemaContextBuilderException{

    private TableKey foreignTableKey;
    private TableKey primaryTableKey;
    private Set<String> foreignKeys;
    private Set<String> primaryKeys;

    public IncorrectForeignKeysMappingException(String message, TableKey foreignTableKey, TableKey primaryTableKey, Set<String> foreignKeys, Set<String> primaryKeys) {
        super(message);
        this.foreignTableKey = foreignTableKey;
        this.primaryTableKey = primaryTableKey;
        this.foreignKeys = foreignKeys;
        this.primaryKeys = primaryKeys;
    }

    public TableKey getForeignTableKey() {
        return foreignTableKey;
    }

    public TableKey getPrimaryTableKey() {
        return primaryTableKey;
    }

    public Set<String> getForeignKeys() {
        return foreignKeys;
    }

    public Set<String> getPrimaryKeys() {
        return primaryKeys;
    }


}
