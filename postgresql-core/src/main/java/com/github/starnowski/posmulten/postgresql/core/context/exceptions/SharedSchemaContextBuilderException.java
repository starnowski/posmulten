package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

public abstract class SharedSchemaContextBuilderException extends Exception{

    public SharedSchemaContextBuilderException() {
    }

    public SharedSchemaContextBuilderException(String message) {
        super(message);
    }

    public SharedSchemaContextBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SharedSchemaContextBuilderException(Throwable cause) {
        super(cause);
    }

    public SharedSchemaContextBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
