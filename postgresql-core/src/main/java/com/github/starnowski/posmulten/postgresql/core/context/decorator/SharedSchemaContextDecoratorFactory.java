package com.github.starnowski.posmulten.postgresql.core.context.decorator;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

public class SharedSchemaContextDecoratorFactory {

    private final List<ISharedSchemaContextDecoratorFactory> factories;

    public SharedSchemaContextDecoratorFactory() {
        this(singletonList(new BasicSharedSchemaContextDecoratorFactory()));
    }

    public SharedSchemaContextDecoratorFactory(List<ISharedSchemaContextDecoratorFactory> factories) {
        this.factories = Collections.unmodifiableList(Optional.ofNullable(factories).orElse(new ArrayList<>()));
    }

    public List<ISharedSchemaContextDecoratorFactory> getFactories() {
        return factories;
    }

    public ISharedSchemaContextDecorator build(ISharedSchemaContext sharedSchemaContext, DefaultDecoratorContext decoratorContext) {
        ISharedSchemaContextDecorator result = null;
        for (ISharedSchemaContextDecoratorFactory factory : factories) {
            result = factory.build(sharedSchemaContext, decoratorContext);
            sharedSchemaContext = result;
        }
        return result;
    }
}
