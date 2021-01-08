package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration;

public class ForeignKeyConfigurationMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration, ForeignKeyConfiguration> {
    @Override
    public ForeignKeyConfiguration map(com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration input) {
        return input == null ? null : new ForeignKeyConfiguration().setConstraintName(input.getConstraintName());
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration unmap(ForeignKeyConfiguration output) {
        return null;
    }
}
