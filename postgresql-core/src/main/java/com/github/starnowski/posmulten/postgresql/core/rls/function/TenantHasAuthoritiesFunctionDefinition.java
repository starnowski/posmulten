package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicySupplier;
import com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeSupplier;

import java.util.ArrayList;
import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueEnum.STRING;
import static java.util.stream.Collectors.joining;

public class TenantHasAuthoritiesFunctionDefinition extends DefaultFunctionDefinition implements TenantHasAuthoritiesFunctionInvocationFactory{

    public TenantHasAuthoritiesFunctionDefinition(IFunctionDefinition functionDefinition) {
        super(functionDefinition);
    }

    @Override
    public String returnTenantHasAuthoritiesFunctionInvocation(FunctionArgumentValue tenantIdValue, PermissionCommandPolicySupplier permissionCommandPolicy, RLSExpressionTypeSupplier rlsExpressionType, FunctionArgumentValue table, FunctionArgumentValue schema) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFunctionReference());
        sb.append("(");
        List<String> list = new ArrayList<>();
        list.add(prepareValue(tenantIdValue));
        list.add(permissionCommandPolicy.getPermissionCommandPolicyString());
        list.add(rlsExpressionType.getRLSExpressionTypeString());
        list.add(prepareValue(table));
        list.add(prepareValue(schema));
        sb.append(list.stream().collect(joining(", ")));
        sb.append(")");
        return sb.toString();
    }

    private String prepareValue(FunctionArgumentValue value)
    {
        return value == null ? null : (STRING.equals(value.getType()) ? ("'" + value.getValue() + "'") : value.getValue());
    }
}
