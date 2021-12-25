package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration
import spock.lang.Unroll

import static java.lang.Boolean.FALSE
import static java.lang.Boolean.TRUE

class DefaultSharedSchemaContextBuilderConfigurationEnricherTest extends AbstractBaseTest {

    def tablesEntriesEnricher = Mock(TablesEntriesEnricher)
    def validTenantValueConstraintConfigurationEnricher = Mock(ValidTenantValueConstraintConfigurationEnricher)
    DefaultSharedSchemaContextBuilderConfigurationEnricher tested

    def setup()
    {
        tested = new DefaultSharedSchemaContextBuilderConfigurationEnricher(tablesEntriesEnricher, validTenantValueConstraintConfigurationEnricher)
    }

    @Unroll
    def "should set builder component with specific properties currentTenantIdPropertyType (#currentTenantIdPropertyType), currentTenantIdProperty (#currentTenantIdProperty), getCurrentTenantIdFunctionName (#getCurrentTenantIdFunctionName), setCurrentTenantIdFunctionName (#setCurrentTenantIdFunctionName), equalsCurrentTenantIdentifierFunctionName (#equalsCurrentTenantIdentifierFunctionName)"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def configuration = new SharedSchemaContextConfiguration()
            .setCurrentTenantIdPropertyType(currentTenantIdPropertyType)
            .setCurrentTenantIdProperty(currentTenantIdProperty)
            .setGetCurrentTenantIdFunctionName(getCurrentTenantIdFunctionName)
            .setSetCurrentTenantIdFunctionName(setCurrentTenantIdFunctionName)
            .setEqualsCurrentTenantIdentifierFunctionName(equalsCurrentTenantIdentifierFunctionName)

        when:
            def result = tested.enrich(builder, configuration)

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

    @Unroll
    def "should set builder component with specific properties tenantHasAuthoritiesFunctionName (#tenantHasAuthoritiesFunctionName), forceRowLevelSecurityForTableOwner (#forceRowLevelSecurityForTableOwner), defaultTenantIdColumn (#defaultTenantIdColumn), grantee (#grantee), currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables (#currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables)"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def configuration = new SharedSchemaContextConfiguration()
                    .setTenantHasAuthoritiesFunctionName(tenantHasAuthoritiesFunctionName)
                    .setForceRowLevelSecurityForTableOwner(forceRowLevelSecurityForTableOwner)
                    .setDefaultTenantIdColumn(defaultTenantIdColumn)
                    .setGrantee(grantee)
                    .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables)

        when:
            def result = tested.enrich(builder, configuration)

        then:
            result == builder
            1 * builder.setTenantHasAuthoritiesFunctionName(tenantHasAuthoritiesFunctionName)
            1 * builder.setForceRowLevelSecurityForTableOwner(forceRowLevelSecurityForTableOwner)
            1 * builder.setDefaultTenantIdColumn(defaultTenantIdColumn)
            1 * builder.setGrantee(grantee)
            1 * builder.setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables)


        where:
            tenantHasAuthoritiesFunctionName    |   forceRowLevelSecurityForTableOwner  |   defaultTenantIdColumn   |   grantee         |   currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables
            "t_has_au"                          |   TRUE                                |   "ten"                   |   "super_user"    |   FALSE
            "is_tenant_allowed_to_access"       |   FALSE                               |   "id_tenant"             |   "i_am_db_owner" |   TRUE
    }

    @Unroll
    def "should use enricher components for entries #validTenantValueConstraintConfiguration and #tablesEntries"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def configuration = new SharedSchemaContextConfiguration()
                    .setValidTenantValueConstraint(validTenantValueConstraintConfiguration)
                    .setTables(tablesEntries)

        when:
            def result = tested.enrich(builder, configuration)

        then:
            result == builder
            1 * validTenantValueConstraintConfigurationEnricher.enrich(builder, validTenantValueConstraintConfiguration)
            1 * tablesEntriesEnricher.enrich(builder, tablesEntries)

        where:
            validTenantValueConstraintConfiguration         |   tablesEntries
            null                                            |   null
            new ValidTenantValueConstraintConfiguration()   |   []
            new ValidTenantValueConstraintConfiguration()   |   [new TableEntry()]
    }

    //TODO Add tests for SQLDefinitionsValidation
}
