package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import static com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition.AT_END;

public class CustomSQLDefinitionsAtEndEnricher extends AbstractCustomSQLDefinitionsEnricher{
    public CustomSQLDefinitionsAtEndEnricher() {
        super(AT_END);
    }
}
