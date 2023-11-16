package com.github.starnowski.posmulten.openwebstart

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSGranteeDeclarationException
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException
import spock.lang.Specification

import javax.swing.JTextArea

class TextAreaExceptionEnricherTest extends Specification {
    def "should enrich JTextArea with error messages for InvalidConfigurationException"() {
        given:
        JTextArea errorTextArea = new JTextArea()
        InvalidConfigurationException ex = new YamlInvalidSchema(["Invalid config", "Some error"])
        def exceptionEnricher = new TextAreaExceptionEnricher()

        when:
            exceptionEnricher.enrich(errorTextArea, ex)

        then:
            errorTextArea.text == "Invalid config" + "\n" + "Some error"
    }

    def "should enrich JTextArea with error message for SharedSchemaContextBuilderException"() {
        given:
            JTextArea errorTextArea = new JTextArea()
            SharedSchemaContextBuilderException ex = new MissingRLSGranteeDeclarationException("Shared schema context error")
            def exceptionEnricher = new TextAreaExceptionEnricher()

        when:
            exceptionEnricher.enrich(errorTextArea, ex)

        then:
            errorTextArea.text == "Shared schema context error"
    }

    def "should enrich JTextArea with error message for RuntimeException"() {
        given:
            JTextArea errorTextArea = new JTextArea()
            RuntimeException ex = new RuntimeException("Runtime error")
            def exceptionEnricher = new TextAreaExceptionEnricher()

        when:
            exceptionEnricher.enrich(errorTextArea, ex)

        then:
            errorTextArea.text.startsWith("Runtime error")
            errorTextArea.text.contains("java.lang.RuntimeException: Runtime error")
    }
}