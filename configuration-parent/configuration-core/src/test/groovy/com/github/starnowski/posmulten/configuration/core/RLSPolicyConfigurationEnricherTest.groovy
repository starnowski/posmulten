package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import spock.lang.Unroll

class RLSPolicyConfigurationEnricherTest extends AbstractBaseTest {

    def tested = new RLSPolicyConfigurationEnricher()

    @Unroll
    def "should set builder component with specific properties currentTenantIdPropertyType (#currentTenantIdPropertyType), currentTenantIdProperty (#currentTenantIdProperty), getCurrentTenantIdFunctionName (#getCurrentTenantIdFunctionName), setCurrentTenantIdFunctionName (#setCurrentTenantIdFunctionName), equalsCurrentTenantIdentifierFunctionName (#equalsCurrentTenantIdentifierFunctionName)"()
    {
        given:
        def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
        def entry = new TableEntry()

        when:
        def result = tested.enrich(builder, entry)

        then:
        result == builder
        1 * builder.setCurrentTenantIdProperty(currentTenantIdProperty)
        1 * builder.setCurrentTenantIdPropertyType(currentTenantIdPropertyType)
        1 * builder.setGetCurrentTenantIdFunctionName(getCurrentTenantIdFunctionName)
        1 * builder.setSetCurrentTenantIdFunctionName(setCurrentTenantIdFunctionName)
        1 * builder.setEqualsCurrentTenantIdentifierFunctionName(equalsCurrentTenantIdentifierFunctionName)


        where:
        currentTenantIdPropertyType |   currentTenantIdProperty |   getCurrentTenantIdFunctionName  |   setCurrentTenantIdFunctionName  |   equalsCurrentTenantIdentifierFunctionName
        "VARCHAR(37)"               |   "customer_d"            |   "what_is_tenant_id"             |   "tenant_is_now"                 |   "is_it_this_tenant_id"
        "UUID"                      |   "tenantId"              |   "get_current_t"                 |   "set_t"                         |   "equals_tenant"
    }

}
