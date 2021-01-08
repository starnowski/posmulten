package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration
import spock.lang.Specification

abstract class AbstractConfigurationMapperTest<I, O, T extends IConfigurationMapper<I, O>> extends Specification {

    abstract protected Class<I> getConfigurationObjectClass()

    abstract protected Class<O> getYamlConfigurationObjectClass()

    abstract protected T getTestedObject()

    abstract protected List<ValidTenantValueConstraintConfiguration> prepareExpectedMappedObjectsList()

    abstract protected List<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> prepareExpectedUmnappeddObjectsList()
}
