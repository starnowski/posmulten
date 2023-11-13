package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

import javax.swing.*;

import static java.util.stream.Collectors.joining;

public class TextAreaExceptionEnricher {

    public void enrich(JTextArea errorTextArea, Exception ex){
        if (ex instanceof InvalidConfigurationException) {
            InvalidConfigurationException e = (InvalidConfigurationException) ex;
            errorTextArea.setText(e.getErrorMessages().stream().collect(joining("\n")));
        } else if (ex instanceof SharedSchemaContextBuilderException) {
            SharedSchemaContextBuilderException e = (SharedSchemaContextBuilderException) ex;
            errorTextArea.setText(ex.getMessage());
        } else if (ex instanceof RuntimeException) {
            RuntimeException e = (RuntimeException) ex;
            errorTextArea.setText(ex.getMessage());
            //TODO Print stacktrace
        }
    }
}
