package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractForeignKeyConfigurationMapperTest

class ForeignKeyConfigurationMapperTest extends AbstractForeignKeyConfigurationMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration, ForeignKeyConfigurationMapper, ConfigurationMapperTestContext> {

    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration createOutputInstance() {
        new com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration()
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration.class
    }

    @Override
    protected ForeignKeyConfigurationMapper getTestedObject() {
        new ForeignKeyConfigurationMapper()
    }
}
