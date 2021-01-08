package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration
import org.jeasy.random.EasyRandom

import static java.util.Arrays.asList

class ValidTenantValueConstraintConfigurationMapperTest extends AbstractConfigurationMapperTest<ValidTenantValueConstraintConfiguration, com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration, ValidTenantValueConstraintConfigurationMapper> {

    def "should unmap random generated yaml configuration object"()
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

    def "should map random generated configuration object"()
    {
        given:
            ValidTenantValueConstraintConfigurationMapper tested = new ValidTenantValueConstraintConfigurationMapper()
            EasyRandom easyRandom = new EasyRandom()
            com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration configuration = easyRandom.nextObject(com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration)

        when:
            def yamlConfiguration = tested.map(configuration)

        then:
            yamlConfiguration

        and: "mapped object should be able to unmap to an equal object"
            configuration == tested.unmap(yamlConfiguration)
    }

    @Override
    protected Class<ValidTenantValueConstraintConfiguration> getConfigurationObjectClass() {
        ValidTenantValueConstraintConfiguration.class
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration.class
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
