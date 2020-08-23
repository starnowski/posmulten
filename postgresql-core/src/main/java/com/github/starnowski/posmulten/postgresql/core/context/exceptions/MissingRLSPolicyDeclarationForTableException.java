package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

public class MissingRLSPolicyDeclarationForTableException extends SharedSchemaContextBuilderException{

    private final TableKey tableKey;

    public MissingRLSPolicyDeclarationForTableException(TableKey tableKey, String message) {
        super(message);
        this.tableKey = tableKey;
    }

    public TableKey getTableKey() {
        return tableKey;
    }
}
