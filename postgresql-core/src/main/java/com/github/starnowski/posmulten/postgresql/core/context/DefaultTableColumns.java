package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class DefaultTableColumns implements AbstractTableColumns{

    private final String tenantColumnName;
    private final Map<String, String> identityColumnNameAndTypeMap;

    public DefaultTableColumns(String tenantColumnName, Map<String, String> identityColumnNameAndTypeMap) {
        this.tenantColumnName = tenantColumnName;
        this.identityColumnNameAndTypeMap = unmodifiableMap(identityColumnNameAndTypeMap);
    }

    @Override
    public String getTenantColumnName() {
        return tenantColumnName;
    }

    @Override
    public Map<String, String> getIdentityColumnNameAndTypeMap() {
        return identityColumnNameAndTypeMap;
    }
}
