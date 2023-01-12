package com.github.starnowski.posmulten.configuration.yaml.context

import com.github.starnowski.posmulten.configuration.yaml.AbstractSpecification
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema
import spock.lang.Unroll

import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.ALL_FIELDS_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.INVALID_LIST_NODES_BLANK_FIELDS_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.INVALID_MAP_BLANK_FIELDS_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.INVALID_NESTED_NODE_BLANK_FIELDS_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.INVALID_NESTED_NODE_EMPTY_LIST_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.INVALID_ROOT_NODE_BLANK_FIELDS_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.ONLY_MANDATORY_FIELDS_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.MANY_TO_MANY_TABLES_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.DISABLED_SQL_DEFINITIONS_VALIDATION_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.SQL_DEFINITIONS_VALIDATION_WITH_CUSTOM_CONSTRAINTS_FILE_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.SQL_DEFINITIONS_VALIDATION_WITH_INVALID_CONSTRAINTS_PATH
import static com.github.starnowski.posmulten.configuration.yaml.TestProperties.SQL_DEFINITIONS_VALIDATION_WITH_NULL_VALUES_PATH

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
            filePath << [ALL_FIELDS_FILE_PATH, ONLY_MANDATORY_FIELDS_FILE_PATH, MANY_TO_MANY_TABLES_FILE_PATH, DISABLED_SQL_DEFINITIONS_VALIDATION_FILE_PATH, SQL_DEFINITIONS_VALIDATION_WITH_CUSTOM_CONSTRAINTS_FILE_PATH, SQL_DEFINITIONS_VALIDATION_WITH_NULL_VALUES_PATH]
    }

    @Unroll
    def "should throw exception that contains error message (#errorMessage) for file #filePath"()
    {
        given:
            def resolvedPath = resolveFilePath(filePath)

        when:
            tested.build(resolvedPath)

        then:
            def ex = thrown(YamlInvalidSchema)
            ex
            ex.getErrorMessages().contains(errorMessage)

        where:
            filePath                                                    ||   errorMessage
            INVALID_ROOT_NODE_BLANK_FIELDS_FILE_PATH                    ||  "grantee must not be blank"
            INVALID_ROOT_NODE_BLANK_FIELDS_FILE_PATH                    ||  "equals_current_tenant_identifier_function_name must not be blank"
            INVALID_NESTED_NODE_BLANK_FIELDS_FILE_PATH                  ||  "valid_tenant_value_constraint.is_tenant_valid_function_name must not be blank"
            INVALID_NESTED_NODE_BLANK_FIELDS_FILE_PATH                  ||  "valid_tenant_value_constraint.tenant_identifiers_blacklist must not be null"
            INVALID_NESTED_NODE_EMPTY_LIST_FILE_PATH                    ||  "valid_tenant_value_constraint.tenant_identifiers_blacklist must have at least one element"
            INVALID_LIST_NODES_BLANK_FIELDS_PATH                        ||  "tables[3].rls_policy.primary_key_definition.name_for_function_that_checks_if_record_exists_in_table must not be blank"
            INVALID_LIST_NODES_BLANK_FIELDS_PATH                        ||  "tables[0].name must not be blank"
            INVALID_MAP_BLANK_FIELDS_PATH                               ||  "tables[0].foreign_keys[0].foreign_key_primary_key_columns_mappings must have at least one element"
            INVALID_MAP_BLANK_FIELDS_PATH                               ||  "tables[2].foreign_keys[2].foreign_key_primary_key_columns_mappings.<map key> must not be blank"
            SQL_DEFINITIONS_VALIDATION_WITH_INVALID_CONSTRAINTS_PATH    ||  "sql_definitions_validation.identifier_min_length must be greater than or equal to 1"
    }

    @Unroll
    def "should throw exception when there is no such file for configuration"()
    {
        when:
            tested.build("no_such_file")

        then:
            def ex = thrown(RuntimeException)
    }
}
