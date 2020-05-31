package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.*;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValueToStringMapper.mapToString;
import static java.util.stream.Collectors.joining;

public class IsRecordBelongsToCurrentTenantFunctionDefinition extends DefaultFunctionDefinition implements IsRecordBelongsToCurrentTenantFunctionInvocationFactory{

    private final List<Pair<String, IFunctionArgument>> keyColumnsPairsList;

    public IsRecordBelongsToCurrentTenantFunctionDefinition(IFunctionDefinition functionDefinition, List<Pair<String, IFunctionArgument>> keyColumnsPairsList) {
        super(functionDefinition);
        this.keyColumnsPairsList = keyColumnsPairsList;
    }

    @Override
    public String returnIsRecordBelongsToCurrentTenantFunctionInvocation(FunctionArgumentValue tenantFunctionArgument, Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        //TODO Validate if
        //TODO tenantFunctionArgument cannot be null
        //TODO tenantFunctionArgument cannot be blank
        //TODO all entries in primaryColumnsValuesMap cannot be null
        //TODO all entries in primaryColumnsValuesMap cannot be blank
        //TODO all items from the keyColumnsPairsList should contains keys in primaryColumnsValuesMap
        StringBuilder sb = new StringBuilder();
        sb.append(getFunctionReference());
        sb.append("(");
        sb.append(this.keyColumnsPairsList.stream()
                .map(Pair::getKey)
                .map(primaryColumnsValuesMap::get)
                .map(FunctionArgumentValueToStringMapper::mapToString)
                .collect(joining(", ")));
        sb.append(", ");
        sb.append(mapToString(tenantFunctionArgument));
        sb.append(")");
        return sb.toString();
    }
}
