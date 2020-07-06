package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;

public interface IsRecordBelongsToCurrentTenantConstraintProducerParameters {

    //TODO not null
    //TODO not empty
    String getConstraintName();

    String getTableName();

    //TODO not empty
    String getTableSchema();

    Map<String, FunctionArgumentValue> getPrimaryColumnsValuesMap();

    //TODO not null
    IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory();
}
