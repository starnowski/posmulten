package com.github.starnowski.posmulten.configuration;

public class FileExtensionExtractor {

    public String extract(String filePath)
    {
        return filePath == null || filePath.trim().isEmpty() ? null : extractExtensionFromNonEmptyValue(filePath);
    }

    private String extractExtensionFromNonEmptyValue(String filePath)
    {
        int i = filePath.lastIndexOf(".");
        String extension = i > 0 && i < filePath.length() ? filePath.substring(i + 1) : null;
        return extension == null || extension.trim().isEmpty() ? null : extension;
    }
}
