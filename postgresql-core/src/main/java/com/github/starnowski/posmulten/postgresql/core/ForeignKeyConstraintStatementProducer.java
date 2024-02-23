package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.rls.AbstractConstraintProducer;
import com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducerParameters;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ForeignKeyConstraintStatementProducer extends AbstractConstraintProducer<IForeignKeyConstraintStatementParameters> {

    @Override
    protected String prepareConstraintBody(IForeignKeyConstraintStatementParameters parameters) {
        String sb = "ALTER TABLE IF EXISTS " + (parameters.getTableSchema() == null ? "\"" + parameters.getTableName() + "\"" : "\"" + parameters.getTableSchema() + "\".\"" + parameters.getTableName() + "\"") +
                " ADD CONSTRAINT " +
                parameters.getConstraintName() +
                " FOREIGN KEY (" +
                parameters.getForeignKeyColumnMappings().keySet().stream().sorted().collect(Collectors.joining(", ")) +
                ") REFERENCES " +
                (parameters.getReferenceTableKey().getSchema() == null ? "\"" + parameters.getReferenceTableKey().getTable() + "\"" : "\"" + parameters.getReferenceTableKey().getSchema() + "\".\"" + parameters.getReferenceTableKey().getTable() + "\"") +
                " (" +
                parameters.getForeignKeyColumnMappings().keySet().stream().sorted().map(key -> parameters.getForeignKeyColumnMappings().get(key)).collect(Collectors.joining(", ")) +
                ") MATCH SIMPLE;";
        return sb;
    }

    public String prepareCreateScript(IForeignKeyConstraintStatementParameters parameters) {
        String sb = "ALTER TABLE IF EXISTS " + (parameters.getTableSchema() == null ? "\"" + parameters.getTableName() + "\"" : "\"" + parameters.getTableSchema() + "\".\"" + parameters.getTableName() + "\"") +
                " ADD CONSTRAINT " +
                parameters.getConstraintName() +
                " FOREIGN KEY (" +
                parameters.getForeignKeyColumnMappings().keySet().stream().sorted().collect(Collectors.joining(", ")) +
                ") REFERENCES " +
                (parameters.getReferenceTableKey().getSchema() == null ? "\"" + parameters.getReferenceTableKey().getTable() + "\"" : "\"" + parameters.getReferenceTableKey().getSchema() + "\".\"" + parameters.getReferenceTableKey().getTable() + "\"") +
                " (" +
                parameters.getForeignKeyColumnMappings().keySet().stream().sorted().map(key -> parameters.getForeignKeyColumnMappings().get(key)).collect(Collectors.joining(", ")) +
                ") MATCH SIMPLE;";
        return sb;
    }
}
