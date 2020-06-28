package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;

public interface IsRecordBelongsToCurrentTenantConstraintProducerParameters {

    String getConstraintName();

    String getTableName();

    String getTableSchema();

    Map<String, FunctionArgumentValue> getPrimaryColumnsValuesMap();

    IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory();
}
