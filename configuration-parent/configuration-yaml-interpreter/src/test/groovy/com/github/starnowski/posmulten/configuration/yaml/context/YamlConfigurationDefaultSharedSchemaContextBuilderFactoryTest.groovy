package com.github.starnowski.posmulten.configuration.yaml.context

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.ALL_FIELDS_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.ONLY_MANDATORY_FIELDS_FILE_PATH

class YamlConfigurationDefaultSharedSchemaContextBuilderFactoryTest extends Specification {

    def tested = new YamlConfigurationDefaultSharedSchemaContextBuilderFactory()

    @Unroll
    def "should create builder component based on file #filePath"()
    {

        where:
            filePath << [ALL_FIELDS_FILE_PATH, ONLY_MANDATORY_FIELDS_FILE_PATH]
    }
}
