package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.rls.IsTenantIdentifierValidConstraintProducer;

public class IsTenantIdentifierValidConstraintEnricher implements ISharedSchemaContextEnricher {

    private final IsTenantIdentifierValidConstraintProducer producer;

    public IsTenantIdentifierValidConstraintEnricher() {
        this(new IsTenantIdentifierValidConstraintProducer());
    }

    public IsTenantIdentifierValidConstraintEnricher(IsTenantIdentifierValidConstraintProducer producer) {
        this.producer = producer;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        return null;
    }
}
