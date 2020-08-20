package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;

public interface AbstractIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters {

    String getConstraintName();

    String getTableName();

    String  getSchema();

    IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory();

    Map<String, String> getForeignKeyPrimaryKeyMappings();
}
