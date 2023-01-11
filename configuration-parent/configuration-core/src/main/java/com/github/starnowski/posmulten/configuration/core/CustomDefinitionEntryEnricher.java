package com.github.starnowski.posmulten.configuration.core;

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry;
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition;
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomDefinitionEntryEnricher {

    private final static Map<String, CustomSQLDefinitionPairPositionProvider> defaultPositionsMap = Stream.of(CustomSQLDefinitionPairDefaultPosition.values()).collect(Collectors.toMap(Enum::name, e -> e));

    public void enrich(DefaultSharedSchemaContextBuilder builder, CustomDefinitionEntry cd) {
        CustomSQLDefinitionPairPositionProvider provider = defaultPositionsMap.containsKey(cd.getPosition().name()) ? defaultPositionsMap.get(cd.getPosition().name()) : new CustomPositionProvider(cd.getCustomPosition());
        builder.addCustomSQLDefinition(provider, cd.getCreationScript(), cd.getDropScript(), cd.getValidationScripts());
    }

    private static final class CustomPositionProvider implements CustomSQLDefinitionPairPositionProvider {

        private final String position;

        public CustomPositionProvider(String position) {
            this.position = position;
        }

        @Override
        public String getPosition() {
            return this.position;
        }
    }
}
