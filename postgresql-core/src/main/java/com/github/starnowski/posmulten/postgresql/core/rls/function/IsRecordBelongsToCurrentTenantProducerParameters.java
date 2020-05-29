package com.github.starnowski.posmulten.postgresql.core.rls.function;

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import javafx.util.Pair;

import java.util.List;

public class IsRecordBelongsToCurrentTenantProducerParameters implements AbstractIsRecordBelongsToCurrentTenantProducerParameters {

    private final String functionName;
    private final String schema;
    private final List<Pair<String, IFunctionArgument>> keyColumnsPairsList;
    private final Pair<String, IFunctionArgument> tenantColumnPair;
    private final String recordTableName;
    private final String recordSchemaName;
    private final IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory;

    public IsRecordBelongsToCurrentTenantProducerParameters(String functionName, String schema, List<Pair<String, IFunctionArgument>> keyColumnsPairsList, Pair<String, IFunctionArgument> tenantColumnPair, String recordTableName, String recordSchemaName, IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory) {
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
    public List<Pair<String, IFunctionArgument>> getKeyColumnsPairsList() {
        return keyColumnsPairsList;
    }

    @Override
    public Pair<String, IFunctionArgument> getTenantColumnPair() {
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

    public static class Builder
    {
        private String functionName;
        private String schema;
        private List<Pair<String, IFunctionArgument>> keyColumnsPairsList;
        private Pair<String, IFunctionArgument> tenantColumnPair;
        private String recordTableName;
        private String recordSchemaName;
        private IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory;

        public Builder withFunctionName(String functionName) {
            this.functionName = functionName;
            return this;
        }

        public Builder withSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public Builder withKeyColumnsPairsList(List<Pair<String, IFunctionArgument>> keyColumnsPairsList) {
            this.keyColumnsPairsList = keyColumnsPairsList;
            return this;
        }

        public Builder withTenantColumnPair(Pair<String, IFunctionArgument> tenantColumnPair) {
            this.tenantColumnPair = tenantColumnPair;
            return this;
        }

        public Builder withRecordTableName(String recordTableName) {
            this.recordTableName = recordTableName;
            return this;
        }

        public Builder withRecordSchemaName(String recordSchemaName) {
            this.recordSchemaName = recordSchemaName;
            return this;
        }

        public Builder withiGetCurrentTenantIdFunctionInvocationFactory(IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory) {
            this.iGetCurrentTenantIdFunctionInvocationFactory = iGetCurrentTenantIdFunctionInvocationFactory;
            return this;
        }

        public IsRecordBelongsToCurrentTenantProducerParameters build()
        {
            return new IsRecordBelongsToCurrentTenantProducerParameters(functionName, schema, keyColumnsPairsList, tenantColumnPair, recordTableName, recordSchemaName, iGetCurrentTenantIdFunctionInvocationFactory);
        }
    }
}
