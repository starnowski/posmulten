package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

import javax.swing.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static java.util.stream.Collectors.joining;

public class TextAreaExceptionEnricher {

    public void enrich(JTextArea errorTextArea, Exception ex){
        if (ex instanceof InvalidConfigurationException) {
            InvalidConfigurationException e = (InvalidConfigurationException) ex;
            errorTextArea.setText(e.getErrorMessages().stream().collect(joining("\n")));
        } else if (ex instanceof SharedSchemaContextBuilderException) {
            SharedSchemaContextBuilderException e = (SharedSchemaContextBuilderException) ex;
            errorTextArea.setText(ex.getMessage());
        } else if (ex instanceof Exception) {
            // Capture the stack trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String stackTrace = sw.toString();

            // Display message and the stack trace in the JTextArea
            errorTextArea.setText(ex.getMessage() + "\n" + stackTrace);
        }
    }
}
