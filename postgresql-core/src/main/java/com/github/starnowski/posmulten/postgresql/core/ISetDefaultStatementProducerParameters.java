package com.github.starnowski.posmulten.postgresql.core;

public interface ISetDefaultStatementProducerParameters {

    String getTable();

    String getColumn();

    String getDefaultValueDefinition();

    String getSchema();
}
