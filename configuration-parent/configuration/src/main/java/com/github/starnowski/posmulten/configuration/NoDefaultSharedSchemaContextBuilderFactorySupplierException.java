package com.github.starnowski.posmulten.configuration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public class NoDefaultSharedSchemaContextBuilderFactorySupplierException extends Exception {

    public NoDefaultSharedSchemaContextBuilderFactorySupplierException(String filePath, Set<String> supportedFileExtensions) {
        super(prepareMessage(filePath, supportedFileExtensions));
    }

    private static String prepareMessage(String filePath, Set<String> supportedFileExtensions) {
        return String.format("No supplier was found, able to handle file %1$s. Supported file extensions: %2$s", filePath, Optional.ofNullable(supportedFileExtensions).orElse(new HashSet<>()).stream().sorted().collect(joining(", ")));
    }
}
