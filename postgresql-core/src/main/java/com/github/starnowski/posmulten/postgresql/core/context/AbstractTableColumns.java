package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.Map;

public interface AbstractTableColumns {

    String getTenantColumnName();

    Map<String, String> getIdentityColumnNameAndTypeMap();
}
