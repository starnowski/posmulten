package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException;

public class IdentifierLengthValidator extends AbstractIdentifierValidator {

    public static final int DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS = 63;

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
        if (sharedSchemaContextRequest.getIdentifierMinLength() != null) {
            if (sharedSchemaContextRequest.getIdentifierMinLength() <= 0) {
                throw new InvalidSharedSchemaContextRequestException("The identifierMinLength property value can not be less or equal to zero");
            }
            this.identifierMinLength = sharedSchemaContextRequest.getIdentifierMinLength();
        } else {
            this.identifierMinLength = 1;
        }
        if (sharedSchemaContextRequest.getIdentifierMaxLength() != null) {
            if (sharedSchemaContextRequest.getIdentifierMaxLength() <= 0) {
                throw new InvalidSharedSchemaContextRequestException("The identifierMaxLength property value can not be less or equal to zero");
            }
            this.identifierMaxLength = sharedSchemaContextRequest.getIdentifierMaxLength();
        } else {
            this.identifierMaxLength = DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS;
        }
    }

    @Override
    public ValidationResult validate(String identifier) {
        if (identifier != null) {
            int length = identifier.length();
            if (length >= identifierMinLength && length <= identifierMaxLength)
            {
                return new ValidationResult(true, "Valid");
            }
        }
        return null;
    }
}
