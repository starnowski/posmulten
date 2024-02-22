package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.context.TableKey;

import java.util.Map;

public class DefaultForeignKeyConstraintStatementParameters implements IForeignKeyConstraintStatementParameters {

    private final Map<String, FunctionArgumentValue> primaryColumnsValuesMap;
    private final TableKey tableKey;
    private final TableKey referenceTableKey;

    public DefaultForeignKeyConstraintStatementParameters(Map<String, FunctionArgumentValue> primaryColumnsValuesMap, TableKey tableKey, TableKey referenceTableKey) {
        this.primaryColumnsValuesMap = primaryColumnsValuesMap;
        this.tableKey = tableKey;
        this.referenceTableKey = referenceTableKey;
    }

    @Override
    public Map<String, FunctionArgumentValue> getPrimaryColumnsValuesMap() {
        return primaryColumnsValuesMap;
    }

    @Override
    public TableKey getTableKey() {
        return tableKey;
    }

    @Override
    public TableKey getReferenceTableKey() {
        return referenceTableKey;
    }

    public static class Builder {
        private Map<String, FunctionArgumentValue> primaryColumnsValuesMap;
        private TableKey tableKey;
        private TableKey referenceTableKey;

        public Builder setPrimaryColumnsValuesMap(Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
            this.primaryColumnsValuesMap = primaryColumnsValuesMap;
            return this;
        }

        public Builder setTableKey(TableKey tableKey) {
            this.tableKey = tableKey;
            return this;
        }

        public Builder setReferenceTableKey(TableKey referenceTableKey) {
            this.referenceTableKey = referenceTableKey;
            return this;
        }

        public DefaultForeignKeyConstraintStatementParameters build() {
            return new DefaultForeignKeyConstraintStatementParameters(this.primaryColumnsValuesMap, this.tableKey, this.referenceTableKey);
        }

    }
}
