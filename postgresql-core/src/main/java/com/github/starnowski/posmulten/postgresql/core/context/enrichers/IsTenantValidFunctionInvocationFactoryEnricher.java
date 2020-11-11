package com.github.starnowski.posmulten.postgresql.core.context.enrichers;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducerParameters;

import java.util.HashSet;

public class IsTenantValidFunctionInvocationFactoryEnricher implements ISharedSchemaContextEnricher {

    private final IsTenantValidBasedOnConstantValuesFunctionProducer isTenantIdentifierValidConstraintProducer;

    public IsTenantValidFunctionInvocationFactoryEnricher() {
        this(new IsTenantValidBasedOnConstantValuesFunctionProducer());
    }

    public IsTenantValidFunctionInvocationFactoryEnricher(IsTenantValidBasedOnConstantValuesFunctionProducer isTenantIdentifierValidConstraintProducer) {
        this.isTenantIdentifierValidConstraintProducer = isTenantIdentifierValidConstraintProducer;
    }

    @Override
    public ISharedSchemaContext enrich(ISharedSchemaContext context, SharedSchemaContextRequest request) throws SharedSchemaContextBuilderException {
        if (request.getTenantValuesBlacklist() != null && !request.getTenantValuesBlacklist().isEmpty())
        {
            String requestFunctionName = request.getIsTenantValidFunctionName();
            String testFunctionName = requestFunctionName == null || requestFunctionName.trim().isEmpty() ? "is_tenant_identifier_valid" : requestFunctionName;
            IsTenantValidBasedOnConstantValuesFunctionDefinition sqlFunctionDefinition = isTenantIdentifierValidConstraintProducer.produce(new IsTenantValidBasedOnConstantValuesFunctionProducerParameters(testFunctionName, request.getDefaultSchema(), new HashSet<String>(request.getTenantValuesBlacklist()), request.getCurrentTenantIdPropertyType()));
            context.addSQLDefinition(sqlFunctionDefinition);
            context.setIIsTenantValidFunctionInvocationFactory(sqlFunctionDefinition);
        }
        return context;
    }
}
