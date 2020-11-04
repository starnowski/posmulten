package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

public class MissingRLSPolicyDeclarationForTableThatRequiredTenatColumnCreationException extends MissingRLSPolicyDeclarationForTableException{

    private final TableKey tableThatRequiredTenantColumnCreation;

    public MissingRLSPolicyDeclarationForTableThatRequiredTenatColumnCreationException(TableKey tableKey, String message, TableKey tableThatRequiredTenantColumnCreation) {
        super(tableKey, message);
        this.tableThatRequiredTenantColumnCreation = tableThatRequiredTenantColumnCreation;
    }

    public TableKey getTableThatRequiredTenantColumnCreation() {
        return tableThatRequiredTenantColumnCreation;
    }

}
