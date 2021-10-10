package com.github.starnowski.posmulten.postgresql.core.context;

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
    public void init(SharedSchemaContextRequest sharedSchemaContextRequest) {

    }

    @Override
    public ValidationResult validate(String identifier) {
        return null;
    }
}
