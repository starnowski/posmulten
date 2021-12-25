package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

public class InvalidIdentifierException extends SharedSchemaContextBuilderException {
    public InvalidIdentifierException(String message) {
        super(message);
    }
}
