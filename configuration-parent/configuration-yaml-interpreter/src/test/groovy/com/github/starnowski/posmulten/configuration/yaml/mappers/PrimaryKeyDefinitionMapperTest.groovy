package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

class PrimaryKeyDefinitionMapperTest extends AbstractConfigurationMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition, com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition, PrimaryKeyDefinitionMapper> {
    @Override
    protected Class<PrimaryKeyDefinition> getConfigurationObjectClass() {
        PrimaryKeyDefinition.class
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition.class
    }

    @Override
    protected PrimaryKeyDefinitionMapper getTestedObject() {
        new PrimaryKeyDefinitionMapper()
    }

    @Override
    protected List<com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition> prepareExpectedMappedObjectsList() {
        [
                new com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition(),
                new com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_for_table_exists"),
                new com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").build()),
                new com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("some_uuid", "UUID").build())
        ]
    }

    @Override
    protected List<PrimaryKeyDefinition> prepareExpectedUnmappeddObjectsList() {
        [
                new PrimaryKeyDefinition(),
                new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_for_table_exists"),
                new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").build()),
                new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("some_uuid", "UUID").build())
        ]
    }
}
