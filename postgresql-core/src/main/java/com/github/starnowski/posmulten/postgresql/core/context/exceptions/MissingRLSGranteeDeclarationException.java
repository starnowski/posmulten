package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

public class MissingRLSGranteeDeclarationException extends SharedSchemaContextBuilderException{

    public MissingRLSGranteeDeclarationException(String message) {
        super(message);
    }
}
