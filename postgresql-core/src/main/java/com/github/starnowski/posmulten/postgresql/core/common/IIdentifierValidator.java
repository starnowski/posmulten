package com.github.starnowski.posmulten.postgresql.core.common;

public interface IIdentifierValidator {

    ValidationResult validate(String identifier);

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
