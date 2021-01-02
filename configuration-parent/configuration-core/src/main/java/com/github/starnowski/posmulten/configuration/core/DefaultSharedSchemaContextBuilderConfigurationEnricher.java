package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class DefaultSharedSchemaContextBuilderConfigurationEnricher {

    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, SharedSchemaContextConfiguration contextConfiguration) {
        //TODO
        if (contextConfiguration.getCurrentTenantIdPropertyType() != null) {
            builder.setCurrentTenantIdPropertyType(contextConfiguration.getCurrentTenantIdPropertyType());
        }
        if (contextConfiguration.getCurrentTenantIdProperty() != null) {
            builder.setCurrentTenantIdProperty(contextConfiguration.getCurrentTenantIdProperty());
        }
        if (contextConfiguration.getGetCurrentTenantIdFunctionName() != null) {
            builder.setGetCurrentTenantIdFunctionName(contextConfiguration.getGetCurrentTenantIdFunctionName());
        }
        if (contextConfiguration.getSetCurrentTenantIdFunctionName() != null)
        {
            builder.setSetCurrentTenantIdFunctionName(contextConfiguration.getSetCurrentTenantIdFunctionName());
        }
        if (contextConfiguration.getEqualsCurrentTenantIdentifierFunctionName() != null)
        {
            builder.setEqualsCurrentTenantIdentifierFunctionName(contextConfiguration.getEqualsCurrentTenantIdentifierFunctionName());
        }
        if (contextConfiguration.getTenantHasAuthoritiesFunctionName() != null)
        {
            builder.setTenantHasAuthoritiesFunctionName(contextConfiguration.getTenantHasAuthoritiesFunctionName());
        }
        if (contextConfiguration.getForceRowLevelSecurityForTableOwner() != null)
        {
            builder.setForceRowLevelSecurityForTableOwner(contextConfiguration.getForceRowLevelSecurityForTableOwner());
        }
        if (contextConfiguration.getDefaultTenantIdColumn() != null)
        {
            builder.setDefaultTenantIdColumn(contextConfiguration.getDefaultTenantIdColumn());
        }
        if (contextConfiguration.getGrantee() != null)
        {
            builder.setGrantee(contextConfiguration.getGrantee());
        }
        if (contextConfiguration.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables() != null)
        {
            builder.setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(contextConfiguration.getCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables());
        }
        return builder;
    }
}
