package com.github.starnowski.posmulten.postgresql.core.context.decorator;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

public class BasicSharedSchemaContextDecorator extends AbstractSharedSchemaContextDecorator {

    private final Map<String, String> variableValueMap;

    public BasicSharedSchemaContextDecorator(ISharedSchemaContext sharedSchemaContext, BasicSharedSchemaContextDecoratorContext basicSharedSchemaContextDecoratorContext) {
        super(sharedSchemaContext);
        this.variableValueMap = unmodifiableMap(ofNullable(basicSharedSchemaContextDecoratorContext.getVariableValueMap()).orElse(new HashMap<>()));
    }

    @Override
    protected String convert(String statement) {
        if (statement == null) {
            return null;
        }
        for (Map.Entry<String, String> entry : variableValueMap.entrySet()) {
            statement = statement.replace(entry.getKey(), entry.getValue());
        }
        return statement;
    }
}
