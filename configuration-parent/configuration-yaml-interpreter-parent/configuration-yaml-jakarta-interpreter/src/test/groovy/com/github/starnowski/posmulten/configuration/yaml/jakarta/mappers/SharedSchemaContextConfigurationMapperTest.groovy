package com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractSharedSchemaContextConfigurationMapperTest
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.TableEntry
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.ValidTenantValueConstraintConfiguration

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
