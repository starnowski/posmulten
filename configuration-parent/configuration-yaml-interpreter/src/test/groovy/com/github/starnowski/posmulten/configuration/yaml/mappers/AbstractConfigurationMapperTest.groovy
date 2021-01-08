package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration
import spock.lang.Specification

import static java.util.stream.Collectors.toList

abstract class AbstractConfigurationMapperTest<I, O, T extends IConfigurationMapper<I, O>> extends Specification {

    def "should unmap yaml objects to expected configuration objects"() {
        given:
            T tested = getTestedObject()
            List<O> yamlObjects = prepareExpectedMappedObjectsList()
            List<I> expectedObjects = prepareExpectedUmnappeddObjectsList()

        when:
            def actualObjects = yamlObjects.stream().map({ yamlObject -> tested.unmap(yamlObject) }).collect(toList())

        then:
            actualObjects == expectedObjects
    }

    abstract protected Class<I> getConfigurationObjectClass()

    abstract protected Class<O> getYamlConfigurationObjectClass()

    abstract protected T getTestedObject()

    abstract protected List<ValidTenantValueConstraintConfiguration> prepareExpectedMappedObjectsList()

    abstract protected List<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> prepareExpectedUmnappeddObjectsList()
}
