package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducer;

public class IIsTenantValidFunctionInvocationFactoryEnricher implements ISharedSchemaContextEnricher {

    private final IsTenantValidBasedOnConstantValuesFunctionProducer isTenantIdentifierValidConstraintProducer;

    public IIsTenantValidFunctionInvocationFactoryEnricher() {
        this(new IsTenantValidBasedOnConstantValuesFunctionProducer());
    }

    public IIsTenantValidFunctionInvocationFactoryEnricher(IsTenantValidBasedOnConstantValuesFunctionProducer isTenantIdentifierValidConstraintProducer) {
        this.isTenantIdentifierValidConstraintProducer = isTenantIdentifierValidConstraintProducer;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        return null;
    }
}
