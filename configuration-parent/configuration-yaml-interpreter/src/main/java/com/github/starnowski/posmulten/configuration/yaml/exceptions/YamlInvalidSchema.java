package com.github.starnowski.posmulten.configuration.yaml.exceptions;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;

import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;

public class YamlInvalidSchema extends InvalidConfigurationException {

    private final List<String> errorMessages;

    public YamlInvalidSchema(List<String> errorMessages) {
        super(prepareExceptionMessage(errorMessages));
        this.errorMessages = unmodifiableList(errorMessages);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    private static String prepareExceptionMessage(List<String> errorMessages) {
        return errorMessages.stream().collect(joining(", "));
    }

}
