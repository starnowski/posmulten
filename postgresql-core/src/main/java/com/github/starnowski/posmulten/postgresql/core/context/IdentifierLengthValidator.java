package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException;

public class IdentifierLengthValidator extends AbstractIdentifierValidator {

    int identifierMaxLength;
    int identifierMinLength;

    int getIdentifierMaxLength() {
        return identifierMaxLength;
    }

    int getIdentifierMinLength() {
        return identifierMinLength;
    }

    @Override
    public void init(SharedSchemaContextRequest sharedSchemaContextRequest) throws InvalidSharedSchemaContextRequestException {
        if (sharedSchemaContextRequest.getIdentifierMinLength() != null && sharedSchemaContextRequest.getIdentifierMinLength() <= 0) {
            throw new InvalidSharedSchemaContextRequestException("The identifierMinLength property value can not be less or equal to zero");
        }
    }

    @Override
    public ValidationResult validate(String identifier) {
        return null;
    }
}
