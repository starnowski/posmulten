package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration
import spock.lang.Specification

import static java.util.Arrays.asList
import static java.util.stream.Collectors.toList

class ValidTenantValueConstraintConfigurationMapperTest extends Specification {

    def "should unmap yaml objects to expected configuration objects"()
    {
        given:
            def tested = new ValidTenantValueConstraintConfigurationMapper()
            List<ValidTenantValueConstraintConfiguration> yamlObjects = prepareExpectedMappedObjectsList()
            List<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> expectedObjects = prepareExpectedUmnappeddObjectsList()

        when:
            def actualObjects = yamlObjects.stream().map({yamlObject -> tested.unmap(yamlObject)}).collect(toList())

        then:
            actualObjects == expectedObjects
    }

    protected List<ValidTenantValueConstraintConfiguration> prepareExpectedMappedObjectsList()
    {
        [
                new ValidTenantValueConstraintConfiguration(),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("tenant_x_id"),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_t_v_constraint"),
                new ValidTenantValueConstraintConfiguration().setIsTenantValidConstraintName("i_t_v_constraint")
                                                            .setIsTenantValidFunctionName("function_name_is_tenant_valid")
                                                            .setTenantIdentifiersBlacklist(asList("XXX", "invalid_tenant"))
        ]
    }

    protected List<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> prepareExpectedUmnappeddObjectsList()
    {
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
