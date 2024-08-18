package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractValidTenantValueConstraintConfigurationMapperTest
import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration

class ValidTenantValueConstraintConfigurationMapperTest extends AbstractValidTenantValueConstraintConfigurationMapperTest<ValidTenantValueConstraintConfiguration, ValidTenantValueConstraintConfigurationMapper, ConfigurationMapperTestContext> {

    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected Class<ValidTenantValueConstraintConfiguration> getYamlConfigurationObjectClass() {
        ValidTenantValueConstraintConfiguration.class
    }

    @Override
    protected ValidTenantValueConstraintConfigurationMapper getTestedObject() {
        new ValidTenantValueConstraintConfigurationMapper()
    }

    @Override
    protected ValidTenantValueConstraintConfiguration createOutputInstance() {
        new ValidTenantValueConstraintConfiguration()
    }
}
