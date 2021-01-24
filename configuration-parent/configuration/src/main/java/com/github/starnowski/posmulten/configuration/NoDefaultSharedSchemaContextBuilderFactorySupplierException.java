package com.github.starnowski.posmulten.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NoDefaultSharedSchemaContextBuilderFactorySupplierException extends Exception {

    private final String filePath;
    private final Set<String> supportedFileExtensions;

    public NoDefaultSharedSchemaContextBuilderFactorySupplierException(String filePath, Set<String> supportedFileExtensions) {
        this.filePath = filePath;
        this.supportedFileExtensions = Collections.unmodifiableSet(supportedFileExtensions == null ? new HashSet<>() : supportedFileExtensions);
    }
}
