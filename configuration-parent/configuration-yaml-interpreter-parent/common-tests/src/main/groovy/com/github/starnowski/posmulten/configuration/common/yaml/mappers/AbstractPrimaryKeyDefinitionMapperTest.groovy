package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractPrimaryKeyDefinition

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

abstract class AbstractPrimaryKeyDefinitionMapperTest<T extends AbstractPrimaryKeyDefinition<T>, M extends IConfigurationMapper<PrimaryKeyDefinition, T> , CMTC extends AbstractConfigurationMapperTestContext> extends AbstractConfigurationMapperTest<T,  PrimaryKeyDefinition, M, CMTC> {
    @Override
    protected Class<PrimaryKeyDefinition> getConfigurationObjectClass() {
        PrimaryKeyDefinition.class
    }

    protected abstract T createOutputInstance();


    @Override
    protected List<T> prepareExpectedMappedObjectsList() {
        [
                createOutputInstance(),
                createOutputInstance().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_for_table_exists"),
                createOutputInstance().setNameForFunctionThatChecksIfRecordExistsInTable("is_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").build()),
                createOutputInstance().setNameForFunctionThatChecksIfRecordExistsInTable("is_exists").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("some_uuid", "UUID").build())
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
