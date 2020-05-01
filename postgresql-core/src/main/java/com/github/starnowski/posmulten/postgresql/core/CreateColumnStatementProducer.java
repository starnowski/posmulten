package com.github.starnowski.posmulten.postgresql.core;

public class CreateColumnStatementProducer {

        public String produce(ICreateColumnStatementProducerParameters parameters){
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
            return "ALTER TABLE " + table + " ADD COLUMN " + column + " " + columnType + ";";
        }
}
