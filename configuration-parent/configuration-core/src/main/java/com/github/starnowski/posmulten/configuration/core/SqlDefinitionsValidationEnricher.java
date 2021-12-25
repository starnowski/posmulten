package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class SqlDefinitionsValidationEnricher {

    public DefaultSharedSchemaContextBuilder enrich(DefaultSharedSchemaContextBuilder builder, SqlDefinitionsValidation sqlDefinitionsValidation) {
        if (sqlDefinitionsValidation != null) {
            builder
                    .setIdentifierMaxLength(sqlDefinitionsValidation.getIdentifierMaxLength())
                    .setIdentifierMinLength(sqlDefinitionsValidation.getIdentifierMinLength());
            if (sqlDefinitionsValidation.getDisabled() != null) {
                builder.setDisableDefaultSqlDefinitionsValidators(sqlDefinitionsValidation.getDisabled());
            }
        }
        return builder;
    }
}
