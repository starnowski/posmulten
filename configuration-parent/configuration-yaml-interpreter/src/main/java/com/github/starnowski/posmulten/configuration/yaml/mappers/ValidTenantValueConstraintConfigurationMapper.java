package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration;

public class ValidTenantValueConstraintConfigurationMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration, ValidTenantValueConstraintConfiguration> {
    @Override
    public ValidTenantValueConstraintConfiguration map(com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration input) {
        return null;
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration unmap(ValidTenantValueConstraintConfiguration output) {
        return output == null ? null : new com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration()
                .setIsTenantValidConstraintName(output.getIsTenantValidConstraintName() == null ? null : output.getIsTenantValidConstraintName().getValue())
                .setIsTenantValidFunctionName(output.getIsTenantValidFunctionName() == null ? null : output.getIsTenantValidFunctionName().getValue())
                .setTenantIdentifiersBlacklist(output.getTenantIdentifiersBlacklist());
    }
}
