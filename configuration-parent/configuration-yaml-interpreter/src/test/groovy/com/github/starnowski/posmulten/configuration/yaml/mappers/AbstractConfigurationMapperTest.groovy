package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration
import org.jeasy.random.EasyRandom
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

    def "should map yaml objects to expected configuration objects"() {
        given:
            T tested = new ValidTenantValueConstraintConfigurationMapper()
            List<I> expectedYamlObjects = prepareExpectedMappedObjectsList()
            List<O> configurationObjects = prepareExpectedUmnappeddObjectsList()

        when:
            def actualObjects = configurationObjects.stream().map({ configurationObject -> tested.map(configurationObject) }).collect(toList())

        then:
            actualObjects == expectedYamlObjects
    }

    def "should unmap random generated yaml configuration object"()
    {
        given:
            T tested = new ValidTenantValueConstraintConfigurationMapper()
            EasyRandom easyRandom = new EasyRandom()
            O yamlConfiguration = easyRandom.nextObject(getYamlConfigurationObjectClass())

        when:
            def configuration = tested.unmap(yamlConfiguration)

        then:
            configuration

        and: "unmapped object should be able to map to an equal object"
            yamlConfiguration == tested.map(configuration)
    }

    abstract protected Class<O> getConfigurationObjectClass()

    abstract protected Class<I> getYamlConfigurationObjectClass()

    abstract protected T getTestedObject()

    abstract protected List<ValidTenantValueConstraintConfiguration> prepareExpectedMappedObjectsList()

    abstract protected List<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> prepareExpectedUmnappeddObjectsList()
}
