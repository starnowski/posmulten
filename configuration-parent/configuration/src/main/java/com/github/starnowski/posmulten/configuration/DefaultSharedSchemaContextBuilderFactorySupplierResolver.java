package com.github.starnowski.posmulten.configuration;

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Comparator.comparingInt;

public class DefaultSharedSchemaContextBuilderFactorySupplierResolver {

    private final FileExtensionExtractor fileExtensionExtractor = new FileExtensionExtractor();

    public IDefaultSharedSchemaContextBuilderFactorySupplier resolveSupplierBasedOnPriorityForFile(String file, Set<IDefaultSharedSchemaContextBuilderFactorySupplier> suppliers) {
        String fileExtension = fileExtensionExtractor.extract(file);
        Comparator<IDefaultSharedSchemaContextBuilderFactorySupplier> priorityComparator = comparingInt(IDefaultSharedSchemaContextBuilderFactorySupplier::getPriority);
        return suppliers.stream().filter(supplier -> extensionIsSupportedBySupplier(fileExtension, supplier.getSupportedFileExtensions())).max(priorityComparator).orElse(null);
    }

    private boolean extensionIsSupportedBySupplier(String fileExtension, List<String> supportedFileExtensions)
    {
        return supportedFileExtensions == null ? false : supportedFileExtensions.stream().anyMatch(supportedFileExtension -> extensionIsEqual(fileExtension, supportedFileExtension));
    }

    private boolean extensionIsEqual(String fileExtension, String supportedExtension)
    {
        fileExtension = fileExtension == null ? null : fileExtension.toLowerCase();
        supportedExtension = supportedExtension == null ? null : supportedExtension.toLowerCase();
        return Objects.equals(fileExtension, supportedExtension);
    }
}
