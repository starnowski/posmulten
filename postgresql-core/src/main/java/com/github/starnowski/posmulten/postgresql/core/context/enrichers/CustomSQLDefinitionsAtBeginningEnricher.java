package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition;

public class CustomSQLDefinitionsAtBeginningEnricher extends AbstractCustomSQLDefinitionsEnricher{
    protected CustomSQLDefinitionsAtBeginningEnricher() {
        super(CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING);
    }
}
