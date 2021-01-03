package com.github.starnowski.posmulten.configuration.core


import com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration
import spock.lang.Unroll

class ValidTenantValueConstraintConfigurationEnricherTest extends AbstractBaseTest {

    def tested = new ValidTenantValueConstraintConfigurationEnricher()

    @Unroll
    def "should set builder component with specific properties tenantValuesBlacklist (#tenantValuesBlacklist), isTenantValidFunctionName (#isTenantValidFunctionName), isTenantValidConstraintName (#isTenantValidConstraintName)"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def configuration = new ValidTenantValueConstraintConfiguration()
                    .setTenantIdentifiersBlacklist(tenantValuesBlacklist)
                    .setIsTenantValidFunctionName(isTenantValidFunctionName)
                    .setIsTenantValidConstraintName(isTenantValidConstraintName)

        when:
            def result = tested.enrich(builder, configuration)

        then:
            result == builder
            1 * builder.createValidTenantValueConstraint(tenantValuesBlacklist, isTenantValidFunctionName, isTenantValidConstraintName)


        where:
            tenantValuesBlacklist   |   isTenantValidFunctionName   |   isTenantValidConstraintName
            ["invalid", "value1"]   |   "is_t_f"                    |   "is_t_c"
            []                      |   null                        |   null
            null                    |   null                        |   null
    }
}
