package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractValidTenantValueConstraintConfiguration

import static java.util.Arrays.asList

abstract class AbstractValidTenantValueConstraintConfigurationMapperTest <T extends AbstractValidTenantValueConstraintConfiguration, M extends IConfigurationMapper< ValidTenantValueConstraintConfiguration, T>, CMTC extends AbstractConfigurationMapperTestContext> extends AbstractConfigurationMapperTest<T, ValidTenantValueConstraintConfiguration, M, CMTC> {

    @Override
    protected Class<ValidTenantValueConstraintConfiguration> getConfigurationObjectClass() {
        ValidTenantValueConstraintConfiguration.class
    }

    protected abstract T createOutputInstance()

    protected List<ValidTenantValueConstraintConfiguration> prepareExpectedMappedObjectsList() {
        [
                createOutputInstance(),
                createOutputInstance().setIsTenantValidConstraintName("tenant_x_id"),
                createOutputInstance().setIsTenantValidConstraintName("i_t_v_constraint"),
                createOutputInstance().setIsTenantValidConstraintName("i_t_v_constraint")
                        .setIsTenantValidFunctionName("function_name_is_tenant_valid")
                        .setTenantIdentifiersBlacklist(asList("XXX", "invalid_tenant"))
        ]
    }

    protected List<ValidTenantValueConstraintConfiguration> prepareExpectedUnmappeddObjectsList() {
        [
                new ValidTenantValueConstraintConfiguration(),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("tenant_x_id"),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_t_v_constraint"),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_t_v_constraint")
                        .setIsTenantValidFunctionName("function_name_is_tenant_valid")
                        .setTenantIdentifiersBlacklist(asList("XXX", "invalid_tenant"))
        ]
    }
}
