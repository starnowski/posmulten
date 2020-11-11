package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

public class MissingRLSPolicyDeclarationForTableThatRequiredTenantColumnCreationException extends MissingRLSPolicyDeclarationForTableException{


    public MissingRLSPolicyDeclarationForTableThatRequiredTenantColumnCreationException(TableKey tableKey, String message) {
        super(tableKey, message);
    }
}
