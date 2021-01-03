package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import spock.lang.Specification

abstract class AbstractBaseTest extends Specification {

    protected DefaultSharedSchemaContextBuilder prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
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
