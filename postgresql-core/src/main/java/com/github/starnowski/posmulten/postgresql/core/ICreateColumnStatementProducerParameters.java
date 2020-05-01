package com.github.starnowski.posmulten.postgresql.core;

public interface ICreateColumnStatementProducerParameters {

    String getTable();

    String getColumn();

    String getColumnType();

    String getSchema();
}
