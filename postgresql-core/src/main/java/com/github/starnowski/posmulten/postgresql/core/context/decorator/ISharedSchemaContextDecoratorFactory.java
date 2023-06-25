package com.github.starnowski.posmulten.postgresql.core.context.decorator;

import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

public interface ISharedSchemaContextDecoratorFactory<T extends ISharedSchemaContextDecorator> {

    T build(ISharedSchemaContext sharedSchemaContext, DefaultDecoratorContext decoratorContext);
}
