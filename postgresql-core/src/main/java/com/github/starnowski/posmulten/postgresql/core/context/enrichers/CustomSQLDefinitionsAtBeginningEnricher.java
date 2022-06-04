package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import static com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING;

public class CustomSQLDefinitionsAtBeginningEnricher extends AbstractCustomSQLDefinitionsEnricher{
    public CustomSQLDefinitionsAtBeginningEnricher() {
        super(AT_BEGINNING);
    }
}
