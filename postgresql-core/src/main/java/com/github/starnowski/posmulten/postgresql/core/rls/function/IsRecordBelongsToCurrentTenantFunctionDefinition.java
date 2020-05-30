package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

import java.util.Map;

public class IsRecordBelongsToCurrentTenantFunctionDefinition extends DefaultFunctionDefinition implements IsRecordBelongsToCurrentTenantFunctionInvocationFactory{

    public IsRecordBelongsToCurrentTenantFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String returnIsRecordBelongsToCurrentTenantFunctionInvocation(FunctionArgumentValue tenantFunctionArgument, Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        return null;
    }
}
