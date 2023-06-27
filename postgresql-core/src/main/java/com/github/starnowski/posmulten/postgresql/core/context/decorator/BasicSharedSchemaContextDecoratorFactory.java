package com.github.starnowski.posmulten.postgresql.core.context.decorator;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

public class BasicSharedSchemaContextDecoratorFactory implements ISharedSchemaContextDecoratorFactory<BasicSharedSchemaContextDecorator> {
    @Override
    public BasicSharedSchemaContextDecorator build(ISharedSchemaContext sharedSchemaContext, DefaultDecoratorContext decoratorContext) {
        return new BasicSharedSchemaContextDecorator(sharedSchemaContext, decoratorContext);
    }
}
