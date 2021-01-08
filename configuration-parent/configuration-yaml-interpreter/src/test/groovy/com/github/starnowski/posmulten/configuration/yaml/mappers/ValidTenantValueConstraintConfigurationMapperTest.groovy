package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration

import static java.util.Arrays.asList

class ValidTenantValueConstraintConfigurationMapperTest extends AbstractConfigurationMapperTest<ValidTenantValueConstraintConfiguration, com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration, ValidTenantValueConstraintConfigurationMapper> {

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> getConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration.class
    }

    @Override
    protected Class<ValidTenantValueConstraintConfiguration> getYamlConfigurationObjectClass() {
        ValidTenantValueConstraintConfiguration.class
    }

    @Override
    protected ValidTenantValueConstraintConfigurationMapper getTestedObject() {
        new ValidTenantValueConstraintConfigurationMapper()
    }

    protected List<ValidTenantValueConstraintConfiguration> prepareExpectedMappedObjectsList() {
        [
                new ValidTenantValueConstraintConfiguration(),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("tenant_x_id"),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_t_v_constraint"),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_t_v_constraint")
                        .setIsTenantValidFunctionName("function_name_is_tenant_valid")
                        .setTenantIdentifiersBlacklist(asList("XXX", "invalid_tenant"))
        ]
    }

    protected List<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> prepareExpectedUmnappeddObjectsList() {
        [
                new com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration(),
                new com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("tenant_x_id"),
                new com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_t_v_constraint"),
                new com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_t_v_constraint")
                        .setIsTenantValidFunctionName("function_name_is_tenant_valid")
                        .setTenantIdentifiersBlacklist(asList("XXX", "invalid_tenant"))
        ]
    }
}
