package com.github.starnowski.posmulten.configuration.common.yaml.dao

import com.github.starnowski.posmulten.configuration.common.yaml.AbstractSpecification
import com.github.starnowski.posmulten.configuration.yaml.core.dao.AbstractSharedSchemaContextConfigurationYamlDao
import com.github.starnowski.posmulten.configuration.yaml.core.exceptions.YamlInvalidSchema
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Paths

import static com.github.starnowski.posmulten.configuration.common.yaml.TestProperties.*

abstract class AbstractSharedSchemaContextConfigurationYamlDaoValidationTest<T extends AbstractSharedSchemaContextConfigurationYamlDao<T>> extends AbstractSpecification {

    protected T tested = getSharedSchemaContextConfigurationYamlDao()

    protected abstract T getSharedSchemaContextConfigurationYamlDao()

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

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for file invalid-list-nodes-blank-fields.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_LIST_NODES_BLANK_FIELDS_PATH)

        when:
            tested.read(resolvedPath)

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["tables[3].rls_policy.primary_key_definition.name_for_function_that_checks_if_record_exists_in_table must not be blank", "tables[2].rls_policy.name must not be blank", "tables[0].name must not be blank"]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for file invalid-map-blank-fields.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_MAP_BLANK_FIELDS_PATH)

        when:
            tested.read(resolvedPath)

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["tables[0].foreign_keys[0].foreign_key_primary_key_columns_mappings must have at least one element",
                             "tables[1].foreign_keys[0].foreign_key_primary_key_columns_mappings.user_id must not be blank",
                             "tables[2].foreign_keys[1].constraint_name must not be blank",
                             "tables[3].foreign_keys[0].foreign_key_primary_key_columns_mappings.user_identi must not be blank",
                             "tables[2].foreign_keys[0].foreign_key_primary_key_columns_mappings must not be null",
                             "tables[2].foreign_keys[2].foreign_key_primary_key_columns_mappings.parent_comment_id must not be blank",
                             "tables[2].foreign_keys[2].foreign_key_primary_key_columns_mappings.<map key> must not be blank",
                             "tables[1].foreign_keys[0].constraint_name must not be blank"
            ]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for file invalid-custom-definitions.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_CUSTOM_DEFINITIONS_FIELDS_PATH)

        when:
            tested.read(resolvedPath)

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["custom_sql_definitions[1].position must not be null",
                             "custom_sql_definitions[3].position available values are AT_END, AT_BEGINNING, CUSTOM",
                             "custom_sql_definitions[0].position must not be null",
                             "custom_sql_definitions[2] for definition with position 'CUSTOM' the property 'custom_position' is required"
            ]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for the content of file invalid-root-node-blank-fields.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_ROOT_NODE_BLANK_FIELDS_FILE_PATH)

        when:
            tested.readFromContent(new String(Files.readAllBytes(Paths.get(resolvedPath))))

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["grantee must not be blank", "default_schema must not be blank", "get_current_tenant_id_function_name must not be blank", "set_current_tenant_id_function_name must not be blank", "equals_current_tenant_identifier_function_name must not be blank"]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for the content of file invalid-nested-node-blank-fields.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_NESTED_NODE_BLANK_FIELDS_FILE_PATH)

        when:
            tested.readFromContent(new String(Files.readAllBytes(Paths.get(resolvedPath))))

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["valid_tenant_value_constraint.is_tenant_valid_function_name must not be blank", "valid_tenant_value_constraint.is_tenant_valid_constraint_name must not be blank", "valid_tenant_value_constraint.tenant_identifiers_blacklist must not be null"]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for the content of file invalid-nested-node-empty-list.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_NESTED_NODE_EMPTY_LIST_FILE_PATH)

        when:
            tested.readFromContent(new String(Files.readAllBytes(Paths.get(resolvedPath))))

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["valid_tenant_value_constraint.tenant_identifiers_blacklist must have at least one element"]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for the content of file invalid-list-nodes-blank-fields.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_LIST_NODES_BLANK_FIELDS_PATH)

        when:
            tested.readFromContent(new String(Files.readAllBytes(Paths.get(resolvedPath))))

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["tables[3].rls_policy.primary_key_definition.name_for_function_that_checks_if_record_exists_in_table must not be blank", "tables[2].rls_policy.name must not be blank", "tables[0].name must not be blank"]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for the content of file invalid-map-blank-fields.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_MAP_BLANK_FIELDS_PATH)

        when:
            tested.readFromContent(new String(Files.readAllBytes(Paths.get(resolvedPath))))

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["tables[0].foreign_keys[0].foreign_key_primary_key_columns_mappings must have at least one element",
                             "tables[1].foreign_keys[0].foreign_key_primary_key_columns_mappings.user_id must not be blank",
                             "tables[2].foreign_keys[1].constraint_name must not be blank",
                             "tables[3].foreign_keys[0].foreign_key_primary_key_columns_mappings.user_identi must not be blank",
                             "tables[2].foreign_keys[0].foreign_key_primary_key_columns_mappings must not be null",
                             "tables[2].foreign_keys[2].foreign_key_primary_key_columns_mappings.parent_comment_id must not be blank",
                             "tables[2].foreign_keys[2].foreign_key_primary_key_columns_mappings.<map key> must not be blank",
                             "tables[1].foreign_keys[0].constraint_name must not be blank"
            ]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for the content of file invalid-custom-definitions.yaml"()
    {
        given:
            def resolvedPath = resolveFilePath(INVALID_CUSTOM_DEFINITIONS_FIELDS_PATH)

        when:
            tested.readFromContent(new String(Files.readAllBytes(Paths.get(resolvedPath))))

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            errorMessage << ["custom_sql_definitions[1].position must not be null",
                             "custom_sql_definitions[3].position available values are AT_END, AT_BEGINNING, CUSTOM",
                             "custom_sql_definitions[0].position must not be null",
                             "custom_sql_definitions[2] for definition with position 'CUSTOM' the property 'custom_position' is required"
            ]
    }
}
