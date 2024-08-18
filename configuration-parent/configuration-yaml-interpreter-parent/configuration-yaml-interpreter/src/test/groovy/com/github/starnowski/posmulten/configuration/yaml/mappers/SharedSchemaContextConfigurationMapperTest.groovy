package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractSharedSchemaContextConfigurationMapperTest
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.yaml.model.TableEntry
import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration

class SharedSchemaContextConfigurationMapperTest extends AbstractSharedSchemaContextConfigurationMapperTest<TableEntry, ValidTenantValueConstraintConfiguration, SharedSchemaContextConfiguration, SharedSchemaContextConfigurationMapper, ConfigurationMapperTestContext> {
    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected SharedSchemaContextConfiguration createOutputInstance() {
        new SharedSchemaContextConfiguration()
    }

    @Override
    protected ValidTenantValueConstraintConfiguration createValidTenantValueConstraintConfigurationInstance() {
        new ValidTenantValueConstraintConfiguration()
    }

    @Override
    protected TableEntry createTableEntryInstance() {
        new TableEntry()
    }

    @Override
    protected Class<SharedSchemaContextConfiguration> getYamlConfigurationObjectClass() {
        SharedSchemaContextConfiguration.class
    }

    @Override
    protected SharedSchemaContextConfigurationMapper getTestedObject() {
        new SharedSchemaContextConfigurationMapper()
    }
}
