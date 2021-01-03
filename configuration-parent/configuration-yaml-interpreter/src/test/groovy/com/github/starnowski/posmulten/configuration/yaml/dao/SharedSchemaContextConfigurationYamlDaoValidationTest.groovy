package com.github.starnowski.posmulten.configuration.yaml.dao

import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema
import spock.lang.Unroll

import java.nio.file.Paths

class SharedSchemaContextConfigurationYamlDaoValidationTest extends spock.lang.Specification {

    public static final INVALID_ROOT_NODE_BLANK_FIELDS_FILE_PATH = "/com/github/starnowski/posmulten/configuration/yaml/invalid-root-node-blank-fields.yaml"
    public static final INVALID_NESTED_NODE_BLANK_FIELDS_FILE_PATH = "/com/github/starnowski/posmulten/configuration/yaml/invalid-nested-node-blank-fields.yaml"
    public static final INVALID_NESTED_NODE_EMPTY_LIST_FILE_PATH = "/com/github/starnowski/posmulten/configuration/yaml/invalid-nested-node-empty-list.yaml"

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
            errorMessage << ["grantee must not be blank", "default_schema must not be blank", "get_current_tenant_id_function_name must not be blank", "set_current_tenant_id_function_name must not be blank", "equals_current_tenant_identifier_function_name must not be blank"]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for file invalid-nested-node-blank-fields.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_NESTED_NODE_BLANK_FIELDS_FILE_PATH)

        when:
            tested.read(resolvedPath)

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["valid_tenant_value_constraint.is_tenant_valid_function_name must not be blank", "valid_tenant_value_constraint.is_tenant_valid_constraint_name must not be blank", "valid_tenant_value_constraint.tenant_identifiers_blacklist must not be null"]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for file invalid-nested-node-empty-list.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_NESTED_NODE_EMPTY_LIST_FILE_PATH)

        when:
            tested.read(resolvedPath)

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["valid_tenant_value_constraint.tenant_identifiers_blacklist must have at least one element"]
    }

    private String resolveFilePath(String filePath) {
        Paths.get(this.class.getResource(filePath).toURI()).toFile().getPath()
    }
}
