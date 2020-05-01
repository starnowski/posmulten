package com.github.starnowski.posmulten.postgresql.core;

public class CreateColumnStatementProducer {

        public String produce(ICreateColumnStatementProducerParameters parameters){
            if (parameters == null)
            {
                throw new IllegalArgumentException("The parameters object cannot be null");
            }
            String table = parameters.getTable();
            String  column = parameters.getColumn();
            String columnType = parameters.getColumnType();
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
            if (columnType == null) {
                throw new IllegalArgumentException("Statement for column type cannot be null");
            }
            if (columnType.trim().isEmpty()) {
                throw new IllegalArgumentException("Statement for column type cannot be blank");
            }
            String schema = parameters.getSchema();
            String tableReference = schema == null ? table : schema + "." + table;
            return "ALTER TABLE " + tableReference + " ADD COLUMN " + column + " " + columnType + ";";
        }
}
