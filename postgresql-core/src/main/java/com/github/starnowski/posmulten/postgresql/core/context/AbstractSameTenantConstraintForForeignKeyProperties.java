package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.Map;

public interface AbstractSameTenantConstraintForForeignKeyProperties {

    Map<String, String> getForeignKeyPrimaryKeyColumnsMappings();

    String getConstraintName();
}
