package com.github.starnowski.posmulten.postgresql.core;

public class CreateColumnStatementProducer {

        public String produce(String table, String column, String columnType){
            return "ALTER TABLE " + table + " ADD COLUMN " + column + " " + columnType + ";";
        }
}
