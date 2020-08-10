package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

public class SetDefaultStatementProducer {

    public SQLDefinition produce(ISetDefaultStatementProducerParameters parameters) {
        if (parameters == null)
        {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        String table = parameters.getTable();
        String  column = parameters.getColumn();
        String defaultValueDefinition = parameters.getDefaultValueDefinition();
        if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (table.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be blank");
        }
        if (column == null) {
            throw new IllegalArgumentException("Column name cannot be null");
        }
        if (column.trim().isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be blank");
        }
        if (defaultValueDefinition == null) {
            throw new IllegalArgumentException("Statement for default value cannot be null");
        }
        if (defaultValueDefinition.trim().isEmpty()) {
            throw new IllegalArgumentException("Statement for default value cannot be blank");
        }
        return new DefaultSQLDefinition(prepareCreateScript(parameters), prepareDropScript(parameters));
    }

    private String prepareDropScript(ISetDefaultStatementProducerParameters parameters) {
        String table = parameters.getTable();
        String  column = parameters.getColumn();
        String schema = parameters.getSchema();
        String tableReference = schema == null ? table : schema + "." + table;
        return "ALTER TABLE " + tableReference + " ALTER COLUMN " + column + " DROP DEFAULT;";
    }

    private String prepareCreateScript(ISetDefaultStatementProducerParameters parameters) {
        String table = parameters.getTable();
        String  column = parameters.getColumn();
        String defaultValueDefinition = parameters.getDefaultValueDefinition();
        String schema = parameters.getSchema();
        String tableReference = schema == null ? table : schema + "." + table;
        return "ALTER TABLE " + tableReference + " ALTER COLUMN " + column + " SET DEFAULT " + defaultValueDefinition + ";";
    }
}
