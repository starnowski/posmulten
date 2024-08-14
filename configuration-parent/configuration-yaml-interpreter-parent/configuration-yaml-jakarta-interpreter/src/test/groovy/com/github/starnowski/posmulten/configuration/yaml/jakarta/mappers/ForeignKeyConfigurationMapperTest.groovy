package com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractForeignKeyConfigurationMapperTest
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.ForeignKeyConfiguration

class ForeignKeyConfigurationMapperTest extends AbstractForeignKeyConfigurationMapperTest<ForeignKeyConfiguration, ForeignKeyConfigurationMapper, ConfigurationMapperTestContext> {

    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected ForeignKeyConfiguration createOutputInstance() {
        new ForeignKeyConfiguration()
    }

    @Override
    protected Class<ForeignKeyConfiguration> getYamlConfigurationObjectClass() {
        ForeignKeyConfiguration.class
    }

    @Override
    protected ForeignKeyConfigurationMapper getTestedObject() {
        new ForeignKeyConfigurationMapper()
    }
}
