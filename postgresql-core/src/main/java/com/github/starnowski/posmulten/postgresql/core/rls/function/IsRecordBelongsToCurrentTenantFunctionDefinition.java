package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.*;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class IsRecordBelongsToCurrentTenantFunctionDefinition extends DefaultFunctionDefinition implements IsRecordBelongsToCurrentTenantFunctionInvocationFactory{

    private final List<Pair<String, IFunctionArgument>> keyColumnsPairsList;

    public IsRecordBelongsToCurrentTenantFunctionDefinition(IFunctionDefinition functionDefinition, List<Pair<String, IFunctionArgument>> keyColumnsPairsList) {
        super(functionDefinition);
        this.keyColumnsPairsList = keyColumnsPairsList;
    }

    @Override
    public String returnIsRecordBelongsToCurrentTenantFunctionInvocation(Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        validate(primaryColumnsValuesMap);
        StringBuilder sb = new StringBuilder();
        sb.append(getFunctionReference());
        sb.append("(");
        sb.append(this.keyColumnsPairsList.stream()
                .map(Pair::getKey)
                .map(primaryColumnsValuesMap::get)
                .map(FunctionArgumentValueToStringMapper::mapToString)
                .collect(joining(", ")));
        sb.append(")");
        return sb.toString();
    }

    private void validate(Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
        if (primaryColumnsValuesMap == null)
        {
            throw new IllegalArgumentException("The primary columns values map cannot be null");
        }
        if (primaryColumnsValuesMap.isEmpty())
        {
            throw new IllegalArgumentException("The primary columns values map cannot be empty");
        }
        if (keyColumnsPairsList.size() != primaryColumnsValuesMap.size())
        {
            throw new IllegalArgumentException(format("The primary columns values map has invalid size, expected %s elements but has %s elements", keyColumnsPairsList.size(), primaryColumnsValuesMap.size()));
        }
        primaryColumnsValuesMap.entrySet().forEach(entry -> {
            if (entry.getKey().trim().isEmpty())
            {
                throw new IllegalArgumentException("The primary columns values map contains an entry with an empty key");
            }
        });
        List<String> missingKeys = keyColumnsPairsList.stream()
                .map(pair -> pair.getKey())
                .filter(key -> !primaryColumnsValuesMap.containsKey(key))
                .collect(toList());
        if (!missingKeys.isEmpty())
        {
            throw new IllegalArgumentException(format("The primary columns values map does not contains keys for function arguments: %s", missingKeys.stream().sorted().collect(joining(", "))));
        }
    }
}
