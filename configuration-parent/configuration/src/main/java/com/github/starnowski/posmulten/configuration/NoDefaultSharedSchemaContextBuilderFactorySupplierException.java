package com.github.starnowski.posmulten.configuration;

import java.util.List;

public class NoDefaultSharedSchemaContextBuilderFactorySupplierException extends Exception {

    private final String filePath;
    private final List<String> supportedFileExtensions;

    public NoDefaultSharedSchemaContextBuilderFactorySupplierException(String filePath, List<String> supportedFileExtensions) {
        this.filePath = filePath;
        this.supportedFileExtensions = supportedFileExtensions;
    }
}
