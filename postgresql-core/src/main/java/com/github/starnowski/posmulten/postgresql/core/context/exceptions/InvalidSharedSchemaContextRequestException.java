package com.github.starnowski.posmulten.postgresql.core.context.exceptions;

public class InvalidSharedSchemaContextRequestException extends SharedSchemaContextBuilderException{
    public InvalidSharedSchemaContextRequestException(String message) {
        super(message);
    }
}
