package com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractRLSPolicyMapperTest
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.PrimaryKeyDefinition
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.RLSPolicy

class RLSPolicyMapperTest extends AbstractRLSPolicyMapperTest<PrimaryKeyDefinition, RLSPolicy, RLSPolicyMapper, ConfigurationMapperTestContext> {

    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected RLSPolicy createOutputInstance() {
        new RLSPolicy()
    }

    @Override
    protected PrimaryKeyDefinition createPrimaryKeyDefinitionInstance() {
        new PrimaryKeyDefinition()
    }

    @Override
    protected Class<RLSPolicy> getYamlConfigurationObjectClass() {
        RLSPolicy.class
    }

    @Override
    protected RLSPolicyMapper getTestedObject() {
        new RLSPolicyMapper()
    }
}
