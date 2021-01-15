package com.github.starnowski.posmulten.configuration.core.context;

public abstract class AbstractDefaultSharedSchemaContextBuilderFactorySupplier implements IDefaultSharedSchemaContextBuilderFactorySupplier {

    @Override
    public int getPriority() {
        return 0;
    }
}
