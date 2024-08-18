package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractCustomDefinitionEntryMapperTest
import com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry


class CustomDefinitionEntryMapperTest extends AbstractCustomDefinitionEntryMapperTest<CustomDefinitionEntry, CustomDefinitionEntryMapper, ConfigurationMapperTestContext> {

    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected CustomDefinitionEntry createOutputInstance() {
        new CustomDefinitionEntry()
    }

    @Override
    protected Class<CustomDefinitionEntry> getYamlConfigurationObjectClass() {
        CustomDefinitionEntry.class
    }

    @Override
    protected CustomDefinitionEntryMapper getTestedObject() {
        new CustomDefinitionEntryMapper()
    }
}
