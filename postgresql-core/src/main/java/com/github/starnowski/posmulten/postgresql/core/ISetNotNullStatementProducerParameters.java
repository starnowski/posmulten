package com.github.starnowski.posmulten.postgresql.core;

public interface ISetNotNullStatementProducerParameters {

    String getTable();

    String getColumn();

    String getSchema();
}
