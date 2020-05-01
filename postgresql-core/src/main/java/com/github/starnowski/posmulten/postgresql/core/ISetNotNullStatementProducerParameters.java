package com.github.starnowski.posmulten.postgresql.core;

public interface ISetNotNullStatementProducerParameters {

    public String getTable();

    public String getColumn();

    public String getSchema();
}
