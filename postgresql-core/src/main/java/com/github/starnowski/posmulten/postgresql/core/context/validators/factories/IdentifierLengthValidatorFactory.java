package com.github.starnowski.posmulten.postgresql.core.context.validators.factories;

import com.github.starnowski.posmulten.postgresql.core.context.IdentifierLengthValidator;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException;

import static com.github.starnowski.posmulten.postgresql.core.context.IdentifierLengthValidator.DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS;

public class IdentifierLengthValidatorFactory implements IIdentifierValidatorFactory<IdentifierLengthValidator> {

    @Override
    public IdentifierLengthValidator build(SharedSchemaContextRequest sharedSchemaContextRequest) throws
            InvalidSharedSchemaContextRequestException {
        int identifierMinLength;
        int identifierMaxLength;
        if (sharedSchemaContextRequest.getIdentifierMinLength() != null) {
            if (sharedSchemaContextRequest.getIdentifierMinLength() <= 0) {
                throw new InvalidSharedSchemaContextRequestException("The identifierMinLength property value can not be less or equal to zero");
            }
            identifierMinLength = sharedSchemaContextRequest.getIdentifierMinLength();
        } else {
            identifierMinLength = 1;
        }
        if (sharedSchemaContextRequest.getIdentifierMaxLength() != null) {
            if (sharedSchemaContextRequest.getIdentifierMaxLength() <= 0) {
                throw new InvalidSharedSchemaContextRequestException("The identifierMaxLength property value can not be less or equal to zero");
            }
            identifierMaxLength = sharedSchemaContextRequest.getIdentifierMaxLength();
        } else {
            identifierMaxLength = DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS;
        }
        return new IdentifierLengthValidator(identifierMinLength, identifierMaxLength);
    }
}
