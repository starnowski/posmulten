package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;

import java.util.Map;

public interface IsRecordBelongsToCurrentTenantFunctionInvocationFactory {

    String returnIsRecordBelongsToCurrentTenantFunctionInvocation(FunctionArgumentValue tenantFunctionArgument, Map<String, FunctionArgumentValue> primaryColumnsValuesMap);
}
