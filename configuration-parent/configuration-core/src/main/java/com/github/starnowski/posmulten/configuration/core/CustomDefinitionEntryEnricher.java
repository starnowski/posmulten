package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry;
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public class CustomDefinitionEntryEnricher {
    public void enrich(DefaultSharedSchemaContextBuilder builder, CustomDefinitionEntry cd) {
        CustomSQLDefinitionPairDefaultPosition defaultPosition = CustomSQLDefinitionPairDefaultPosition.valueOf(cd.getPosition().name());
        builder.addCustomSQLDefinition(defaultPosition == null ? () -> cd.getCustomPosition() : defaultPosition, cd.getCreationScript(), cd.getDropScript(), cd.getValidationScripts());
    }
}
