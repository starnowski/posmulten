package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition;

public class PrimaryKeyDefinitionMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition, PrimaryKeyDefinition> {
    @Override
    public PrimaryKeyDefinition map(com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition input) {
        return input == null ? null : new PrimaryKeyDefinition()
                .setNameForFunctionThatChecksIfRecordExistsInTable(input.getNameForFunctionThatChecksIfRecordExistsInTable())
                .setPrimaryKeyColumnsNameToTypeMap(input.getPrimaryKeyColumnsNameToTypeMap());
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition unmap(PrimaryKeyDefinition output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition()
                .setNameForFunctionThatChecksIfRecordExistsInTable(output.getNameForFunctionThatChecksIfRecordExistsInTable())
                .setPrimaryKeyColumnsNameToTypeMap(output.getPrimaryKeyColumnsNameToTypeMap());
    }
}
