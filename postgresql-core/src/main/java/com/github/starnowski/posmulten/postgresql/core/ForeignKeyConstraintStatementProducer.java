package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.rls.AbstractConstraintProducer;

import java.util.stream.Collectors;

public class ForeignKeyConstraintStatementProducer extends AbstractConstraintProducer<IForeignKeyConstraintStatementParameters> {

    @Override
    protected String prepareConstraintBody(IForeignKeyConstraintStatementParameters parameters) {
        return "";
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

    @Override
    protected void validate(IForeignKeyConstraintStatementParameters parameters) {
        super.validate(parameters);
        if (parameters.getReferenceTableKey() == null) {
            throw new IllegalArgumentException("Reference table key object can not be null");
        }
        if (parameters.getReferenceTableKey().getTable() == null || parameters.getReferenceTableKey().getTable().trim().isEmpty()) {
            throw new IllegalArgumentException("Reference table can not be null or empty");
        }
        if (parameters.getReferenceTableKey().getSchema() != null && parameters.getReferenceTableKey().getSchema().trim().isEmpty()) {
            throw new IllegalArgumentException("Reference schema can not be empty");
        }
    }
}
