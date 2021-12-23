package com.github.starnowski.posmulten.postgresql.core.context;

import static java.lang.String.format;

public class IdentifierLengthValidator extends AbstractIdentifierValidator {

    public static final int DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS = 63;

    private final int identifierMaxLength;
    private final int identifierMinLength;

    public IdentifierLengthValidator() {
        this(0, DEFAULT_MAXIMUM_NUMBER_OF_IDENTIFIER_CHARACTERS);
    }

    public IdentifierLengthValidator(int identifierMinLength, int identifierMaxLength) {
        this.identifierMaxLength = identifierMaxLength;
        this.identifierMinLength = identifierMinLength;
    }

    public int getIdentifierMaxLength() {
        return identifierMaxLength;
    }

    public int getIdentifierMinLength() {
        return identifierMinLength;
    }

    @Override
    public ValidationResult validate(String identifier) {
        if (identifier != null) {
            int length = identifier.length();
            if (length >= identifierMinLength && length <= identifierMaxLength) {
                return new ValidationResult(true, "Valid");
            } else {
                return new ValidationResult(false, format("Identifier '%s' is invalid, the length must be between %d and %d", identifier, identifierMinLength, identifierMaxLength));
            }
        }
        return new ValidationResult(false, "Identifier cannot be null");
    }
}
