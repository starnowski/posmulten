package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier;

import java.util.Comparator;
import java.util.Set;

import static java.util.Comparator.comparingInt;

public class DefaultSharedSchemaContextBuilderFactorySupplierResolver {

    public IDefaultSharedSchemaContextBuilderFactorySupplier resolveSupplierBasedOnPriorityForFile(String file, Set<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliers) {
        //TODO
        Comparator<IDefaultSharedSchemaContextBuilderFactorySupplier> priorityComparator = comparingInt(IDefaultSharedSchemaContextBuilderFactorySupplier::getPriority);
        return suppliers.stream().max(priorityComparator).orElse(null);
    }
}
