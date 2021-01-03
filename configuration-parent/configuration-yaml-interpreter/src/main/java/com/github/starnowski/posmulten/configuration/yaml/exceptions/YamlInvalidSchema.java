package com.github.starnowski.posmulten.configuration.yaml.exceptions;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class YamlInvalidSchema extends RuntimeException{

    public YamlInvalidSchema(List<String> errorMessages) {
        super(prepareExceptionMessage(errorMessages));
        this.errorMessages = errorMessages;
    }

    private final List<String> errorMessages;

    private static String prepareExceptionMessage(List<String> errorMessages)
    {
        return errorMessages.stream().collect(joining(", "));
    }

}
