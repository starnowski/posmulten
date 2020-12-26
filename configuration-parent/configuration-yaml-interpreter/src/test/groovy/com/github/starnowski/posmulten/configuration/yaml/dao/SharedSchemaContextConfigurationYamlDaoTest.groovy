package com.github.starnowski.posmulten.configuration.yaml.dao

import spock.lang.Unroll

import java.nio.file.Paths

class SharedSchemaContextConfigurationYamlDaoTest extends spock.lang.Specification {

    public static final ALL_FIELDS_FILE_PATH = "/com/github/starnowski/posmulten/configuration/yaml/all-fields.yaml"

    def tested = new SharedSchemaContextConfigurationYamlDao()

    @Unroll
    def "should return non null object based on file #filePath"()
    {
        given:
            def resolvedPath = Paths.get(this.class.getResource(ALL_FIELDS_FILE_PATH).toURI()).toFile().getPath()

        when:
            def result = tested.read(resolvedPath)

        then:
            result

        where:
            filePath << [ALL_FIELDS_FILE_PATH]
    }
}
