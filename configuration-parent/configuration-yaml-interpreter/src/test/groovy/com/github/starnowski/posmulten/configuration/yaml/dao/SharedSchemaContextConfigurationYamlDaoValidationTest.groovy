package com.github.starnowski.posmulten.configuration.yaml.dao

import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema
import spock.lang.Unroll

import java.nio.file.Paths

class SharedSchemaContextConfigurationYamlDaoValidationTest extends spock.lang.Specification {

    public static final INVALID_ROOT_NODE_BLANK_FIELDS_FILE_PATH = "/com/github/starnowski/posmulten/configuration/yaml/invalid-root-node-blank-fields.yaml"

    def tested = new SharedSchemaContextConfigurationYamlDao()

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for file invalid-root-node-blank-fields.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_ROOT_NODE_BLANK_FIELDS_FILE_PATH)

        when:
            tested.read(resolvedPath)

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["grantee must not be blank", "default_schema must not be blank"]
    }

    private String resolveFilePath(String filePath) {
        Paths.get(this.class.getResource(filePath).toURI()).toFile().getPath()
    }
}
