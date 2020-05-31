package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;

import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper.mapToString;

public class IsRecordBelongsToCurrentTenantFunctionDefinition extends DefaultFunctionDefinition implements IsRecordBelongsToCurrentTenantFunctionInvocationFactory{

    public IsRecordBelongsToCurrentTenantFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String returnIsRecordBelongsToCurrentTenantFunctionInvocation(FunctionArgumentValue tenantFunctionArgument, Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFunctionReference());
        sb.append("(");
        sb.append(", ");
        sb.append(mapToString(tenantFunctionArgument));
        sb.append(")");
        return sb.toString();
    }
}
