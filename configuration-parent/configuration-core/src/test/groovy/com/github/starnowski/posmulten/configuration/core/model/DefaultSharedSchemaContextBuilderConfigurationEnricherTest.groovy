package com.github.starnowski.posmulten.configuration.core.model

import com.github.starnowski.posmulten.configuration.core.DefaultSharedSchemaContextBuilderConfigurationEnricher
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import spock.lang.Specification
import spock.lang.Unroll

import static java.lang.Boolean.FALSE
import static java.lang.Boolean.TRUE

class DefaultSharedSchemaContextBuilderConfigurationEnricherTest extends Specification {

    def tested = new DefaultSharedSchemaContextBuilderConfigurationEnricher()

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
            1 * builder.setCurrentTenantIdPropertyType(forceRowLevelSecurityForTableOwner)
            1 * builder.setDefaultTenantIdColumn(defaultTenantIdColumn)
            1 * builder.setGrantee(grantee)
            1 * builder.setEqualsCurrentTenantIdentifierFunctionName(currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables)


        where:
            tenantHasAuthoritiesFunctionName    |   forceRowLevelSecurityForTableOwner  |   defaultTenantIdColumn   |   grantee         |   currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables
            "t_has_au"                          |   TRUE                                |   "ten"                   |   "super_user"    |   FALSE
            "is_tenant_allowed_to_access"       |   FALSE                               |   "id_tenant"             |   "i_am_db_owner" |   TRUE
    }

    private DefaultSharedSchemaContextBuilder prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
    {
        def builder = Mock(DefaultSharedSchemaContextBuilder)
        0 * builder.setCurrentTenantIdProperty(_)
        0 * builder.setCurrentTenantIdPropertyType(_)
        0 * builder.setGetCurrentTenantIdFunctionName(_)
        0 * builder.setSetCurrentTenantIdFunctionName(_)
        0 * builder.setEqualsCurrentTenantIdentifierFunctionName(_)
        0 * builder.setTenantHasAuthoritiesFunctionName(_)
        0 * builder.setForceRowLevelSecurityForTableOwner(_)
        0 * builder.setDefaultTenantIdColumn(_)
        0 * builder.setGrantee(_)
        0 * builder.setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(_)
        0 * builder.registerCustomValidTenantValueConstraintNameForTable(_, _)
        0 * builder.createValidTenantValueConstraint(_, _, _)
        0 * builder.createRLSPolicyForTable(_, _, _, _)
        0 * builder.createTenantColumnForTable(_)
        0 * builder.skipAddingOfTenantColumnDefaultValueForTable(_)
        0 * builder.registerCustomValidTenantValueConstraintNameForTable(_, _)
        0 * builder.setNameForFunctionThatChecksIfRecordExistsInTable(_, _)
        0 * builder.createSameTenantConstraintForForeignKey(_, _, _, _)
        builder
    }
}
