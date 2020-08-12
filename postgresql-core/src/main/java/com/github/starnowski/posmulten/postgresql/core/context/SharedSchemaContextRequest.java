package com.github.starnowski.posmulten.postgresql.core.context;

public class SharedSchemaContextRequest {

    private String defaultSchema;

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }
}
