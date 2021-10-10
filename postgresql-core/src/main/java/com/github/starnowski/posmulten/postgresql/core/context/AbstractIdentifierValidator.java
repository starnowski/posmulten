package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException;

public abstract class AbstractIdentifierValidator {

    public abstract void init(SharedSchemaContextRequest sharedSchemaContextRequest) throws InvalidSharedSchemaContextRequestException;

    public abstract ValidationResult validate(String identifier);

    class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
