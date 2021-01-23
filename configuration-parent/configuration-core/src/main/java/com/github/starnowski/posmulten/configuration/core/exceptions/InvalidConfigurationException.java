package com.github.starnowski.posmulten.configuration.core.exceptions;

import java.util.List;

public abstract class InvalidConfigurationException extends Exception {

    public InvalidConfigurationException() {
    }

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    public abstract List<String> getErrorMessages();
}
