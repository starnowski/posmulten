package com.github.starnowski.posmulten.configuration.yaml.context

import com.github.starnowski.posmulten.configuration.yaml.AbstractSpecification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.ALL_FIELDS_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.ONLY_MANDATORY_FIELDS_FILE_PATH

class YamlConfigurationDefaultSharedSchemaContextBuilderFactoryTest extends AbstractSpecification {

    def tested = new YamlConfigurationDefaultSharedSchemaContextBuilderFactory()

    @Unroll
    def "should create builder component based on file #filePath"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            def builder = tested.build(resolvedPath)

        then:
            builder

        and: "builder should return a non-empty list of DDL statements"
            !builder.build().getSqlDefinitions().isEmpty()

        where:
            filePath << [ALL_FIELDS_FILE_PATH, ONLY_MANDATORY_FIELDS_FILE_PATH]
    }
}
