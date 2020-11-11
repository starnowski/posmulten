package com.github.starnowski.posmulten.postgresql.core.rls;

public interface IConstraintProducerParameters {

    String getConstraintName();

    String getTableName();

    String getTableSchema();
}
