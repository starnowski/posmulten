package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy;
import com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue;

public class RLSPolicyMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.RLSPolicy, RLSPolicy> {
    @Override
    public RLSPolicy map(com.github.starnowski.posmulten.configuration.core.model.RLSPolicy input) {
        return input == null ? null : new RLSPolicy()
                .setName(input.getName())
                .setCreateTenantColumnForTable(input.getCreateTenantColumnForTable())
                .setNameForFunctionThatChecksIfRecordExistsInTable(input.getNameForFunctionThatChecksIfRecordExistsInTable())
                .setPrimaryKeyColumnsNameToTypeMap(input.getPrimaryKeyColumnsNameToTypeMap())
                .setSkipAddingOfTenantColumnDefaultValue(input.getSkipAddingOfTenantColumnDefaultValue())
                .setTenantColumn(input.getTenantColumn() == null ? null : new StringWrapperWithNotBlankValue(input.getTenantColumn()))
                .setValidTenantValueConstraintName(input.getValidTenantValueConstraintName() == null ? null : new StringWrapperWithNotBlankValue(input.getValidTenantValueConstraintName()));
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.RLSPolicy unmap(RLSPolicy output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.RLSPolicy()
                .setName(output.getName())
                .setCreateTenantColumnForTable(output.getCreateTenantColumnForTable())
                .setNameForFunctionThatChecksIfRecordExistsInTable(output.getNameForFunctionThatChecksIfRecordExistsInTable())
                .setPrimaryKeyColumnsNameToTypeMap(output.getPrimaryKeyColumnsNameToTypeMap())
                .setSkipAddingOfTenantColumnDefaultValue(output.getSkipAddingOfTenantColumnDefaultValue())
                .setTenantColumn(output.getTenantColumn() == null ? null : output.getTenantColumn().getValue())
                .setValidTenantValueConstraintName(output.getValidTenantValueConstraintName() == null ? null : output.getValidTenantValueConstraintName().getValue());
    }
}