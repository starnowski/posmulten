package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractCustomDefinitionEntryMapperTest
import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry


class CustomDefinitionEntryMapperTest extends AbstractCustomDefinitionEntryMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry, CustomDefinitionEntryMapper, ConfigurationMapperTestContext> {

    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected Class<CustomDefinitionEntry> getConfigurationObjectClass() {
        CustomDefinitionEntry.class
    }

    @Override
    protected com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry createOutputInstance() {
        new com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry()
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry.class
    }

    @Override
    protected CustomDefinitionEntryMapper getTestedObject() {
        new CustomDefinitionEntryMapper()
    }
}
