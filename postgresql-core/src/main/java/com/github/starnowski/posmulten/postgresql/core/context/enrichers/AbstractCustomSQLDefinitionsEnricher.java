package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

public abstract class AbstractCustomSQLDefinitionsEnricher implements ISharedSchemaContextEnricher{

    private final CustomSQLDefinitionPairPositionProvider customSQLDefinitionPairPositionProvider;

    protected AbstractCustomSQLDefinitionsEnricher(CustomSQLDefinitionPairPositionProvider customSQLDefinitionPairPositionProvider) {
        this.customSQLDefinitionPairPositionProvider = customSQLDefinitionPairPositionProvider;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        return null;
    }
}
