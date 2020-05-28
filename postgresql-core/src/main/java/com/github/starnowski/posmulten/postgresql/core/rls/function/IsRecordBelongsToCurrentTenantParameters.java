package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import javafx.util.Pair;

import java.util.List;

public class IsRecordBelongsToCurrentTenantParameters implements AbstractIsRecordBelongsToCurrentTenantParameters{

    private final String functionName;
    private final String schema;
    private final List<Pair<String, FunctionArgumentValue>> keyColumnsPairsList;
    private final Pair<String, FunctionArgumentValue> tenantColumnPair;
    private final String recordTableName;
    private final String recordSchemaName;
    private final IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory;

    public IsRecordBelongsToCurrentTenantParameters(String functionName, String schema, List<Pair<String, FunctionArgumentValue>> keyColumnsPairsList, Pair<String, FunctionArgumentValue> tenantColumnPair, String recordTableName, String recordSchemaName, IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory) {
        this.functionName = functionName;
        this.schema = schema;
        this.keyColumnsPairsList = keyColumnsPairsList;
        this.tenantColumnPair = tenantColumnPair;
        this.recordTableName = recordTableName;
        this.recordSchemaName = recordSchemaName;
        this.iGetCurrentTenantIdFunctionInvocationFactory = iGetCurrentTenantIdFunctionInvocationFactory;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public List<Pair<String, FunctionArgumentValue>> getKeyColumnsPairsList() {
        return keyColumnsPairsList;
    }

    @Override
    public Pair<String, FunctionArgumentValue> getTenantColumnPair() {
        return tenantColumnPair;
    }

    @Override
    public String getRecordTableName() {
        return recordTableName;
    }

    @Override
    public String getRecordSchemaName() {
        return recordSchemaName;
    }

    public IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory() {
        return iGetCurrentTenantIdFunctionInvocationFactory;
    }
}
