package com.github.starnowski.posmulten.configuration.core.context;

import java.util.List;
import java.util.function.Supplier;

public interface IDefaultSharedSchemaContextBuilderFactorySupplier {

    Supplier<IDefaultSharedSchemaContextBuilderFactory> getFactorySupplier();

    int getPriority();

    List<String> getSupportedFileExtensions();
}
