package com.github.starnowski.posmulten.configuration.yaml.mappers


import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration
import org.jeasy.random.EasyRandom
import spock.lang.Specification

import static java.util.Arrays.asList
import static java.util.stream.Collectors.toList

class ValidTenantValueConstraintConfigurationMapperTest extends Specification {

    def "should unmap yaml objects to expected configuration objects"() {
        given:
            ValidTenantValueConstraintConfigurationMapper tested = new ValidTenantValueConstraintConfigurationMapper()
            List<ValidTenantValueConstraintConfiguration> yamlObjects = prepareExpectedMappedObjectsList()
            List<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> expectedObjects = prepareExpectedUmnappeddObjectsList()

        when:
            def actualObjects = yamlObjects.stream().map({ yamlObject -> tested.unmap(yamlObject) }).collect(toList())

        then:
            actualObjects == expectedObjects
    }

    def "should map yaml objects to expected configuration objects"() {
        given:
            ValidTenantValueConstraintConfigurationMapper tested = new ValidTenantValueConstraintConfigurationMapper()
            List<ValidTenantValueConstraintConfiguration> expectedYamlObjects = prepareExpectedMappedObjectsList()
            List<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> configurationObjects = prepareExpectedUmnappeddObjectsList()

        when:
            def actualObjects = configurationObjects.stream().map({ configurationObject -> tested.map(configurationObject) }).collect(toList())

        then:
            actualObjects == expectedYamlObjects
    }

    def "should unmap random generated configuration object"()
    {
        given:
            ValidTenantValueConstraintConfigurationMapper tested = new ValidTenantValueConstraintConfigurationMapper()
            EasyRandom easyRandom = new EasyRandom()
            ValidTenantValueConstraintConfiguration yamlConfiguration = easyRandom.nextObject(ValidTenantValueConstraintConfiguration)

        when:
            def configuration = tested.unmap(yamlConfiguration)

        then:
            configuration

        and: "unmapped object should be able to map to an equal object"
            yamlConfiguration == tested.map(configuration)
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
