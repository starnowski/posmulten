package com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractPrimaryKeyDefinitionMapperTest
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.PrimaryKeyDefinition

class PrimaryKeyDefinitionMapperTest extends AbstractPrimaryKeyDefinitionMapperTest<PrimaryKeyDefinition, PrimaryKeyDefinitionMapper, ConfigurationMapperTestContext> {
    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected PrimaryKeyDefinition createOutputInstance() {
        new PrimaryKeyDefinition()
    }

    @Override
    protected Class<PrimaryKeyDefinition> getYamlConfigurationObjectClass() {
        PrimaryKeyDefinition.class
    }

    @Override
    protected PrimaryKeyDefinitionMapper getTestedObject() {
        new PrimaryKeyDefinitionMapper()
    }
}
