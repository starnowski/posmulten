package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;

public interface AbstractIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters {

    String getConstraintName();

    TableKey getTableKey();

    IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory();

    Map<String, String> getForeignKeyPrimaryKeyMappings();
}
