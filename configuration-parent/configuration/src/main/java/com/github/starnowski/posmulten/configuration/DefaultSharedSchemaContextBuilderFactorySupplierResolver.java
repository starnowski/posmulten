package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier;

import java.util.Comparator;
import java.util.Set;

public class DefaultSharedSchemaContextBuilderFactorySupplierResolver {

    public IDefaultSharedSchemaContextBuilderFactorySupplier resolveSupplierBasedOnPriorityForFile(String file, Set<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliers)
    {
        //TODO
        Comparator<IDefaultSharedSchemaContextBuilderFactorySupplier> priorityComparator = Comparator.comparingInt(supplier -> supplier.getPriority());
        return suppliers.stream().sorted(priorityComparator.reversed()).findFirst().orElse(null);
    }
}
