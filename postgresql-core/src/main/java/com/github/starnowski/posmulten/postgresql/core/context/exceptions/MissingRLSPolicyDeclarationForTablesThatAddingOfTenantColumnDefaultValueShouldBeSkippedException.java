package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

public class MissingRLSPolicyDeclarationForTablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedException extends MissingRLSPolicyDeclarationForTableException{
    public MissingRLSPolicyDeclarationForTablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedException(TableKey tableKey, String message) {
        super(tableKey, message);
    }
}
