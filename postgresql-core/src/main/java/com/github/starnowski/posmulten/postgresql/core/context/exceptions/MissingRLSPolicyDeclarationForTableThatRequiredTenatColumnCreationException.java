package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

public class MissingRLSPolicyDeclarationForTableThatRequiredTenatColumnCreationException extends MissingRLSPolicyDeclarationForTableException{


    public MissingRLSPolicyDeclarationForTableThatRequiredTenatColumnCreationException(TableKey tableKey, String message) {
        super(tableKey, message);
    }
}
