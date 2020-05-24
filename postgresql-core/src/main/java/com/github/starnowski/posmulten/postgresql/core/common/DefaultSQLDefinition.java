package com.github.starnowski.posmulten.postgresql.core.common;

public class DefaultSQLDefinition implements SQLDefinition{

    private final String createScript;
    private final String dropScript;

    public DefaultSQLDefinition(String createScript, String dropScript) {
        this.createScript = createScript;
        this.dropScript = dropScript;
    }

    @Override
    public String getCreateScript() {
        return createScript;
    }

    @Override
    public String getDropScript() {
        return dropScript;
    }
}
