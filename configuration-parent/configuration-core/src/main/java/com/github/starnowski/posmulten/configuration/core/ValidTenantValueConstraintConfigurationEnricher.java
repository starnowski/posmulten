package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class ValidTenantValueConstraintConfigurationEnricher {

    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, ValidTenantValueConstraintConfiguration configuration) {
        if (configuration != null)
        {
            builder.createValidTenantValueConstraint(configuration.getTenantIdentifiersBlacklist(), configuration.getIsTenantValidFunctionName(), configuration.getIsTenantValidConstraintName());
        }
        return builder;
    }
}
