package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

public class IsRecordBelongsToCurrentTenantConstraintProducer {

    public SQLDefinition produce(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        validate(parameters);
        return new DefaultSQLDefinition(prepareCreateScript(parameters), prepareDropScript(parameters));
    }

    protected String prepareCreateScript(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ALTER TABLE ");
        stringBuilder.append(prepareTableReference(parameters));
        stringBuilder.append(" ADD CONSTRAINT ");
        stringBuilder.append(parameters.getConstraintName());
        stringBuilder.append(" CHECK ");
        stringBuilder.append("(");
        stringBuilder.append(parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory().returnIsRecordBelongsToCurrentTenantFunctionInvocation(parameters.getPrimaryColumnsValuesMap()));
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    protected void validate(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        if (parameters == null)
        {
            throw new IllegalArgumentException("The parameters object cannot be null");
        }
        if (parameters.getTableName() == null)
        {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        if (parameters.getTableName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Table name cannot be empty");
        }
        if (parameters.getTableSchema() != null && parameters.getTableSchema().trim().isEmpty())
        {
            throw new IllegalArgumentException("Table schema cannot be empty");
        }
        if (parameters.getConstraintName() == null)
        {
            throw new IllegalArgumentException("Constraint name cannot be null");
        }
        if (parameters.getConstraintName().trim().isEmpty())
        {
            throw new IllegalArgumentException("Constraint name cannot be empty");
        }
        if (parameters.getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() == null)
        {
            throw new IllegalArgumentException("Object of type IsRecordBelongsToCurrentTenantFunctionInvocationFactory cannot be null");
        }
    }

    private String prepareTableReference(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if (parameters.getTableSchema() != null)
        {
            stringBuilder.append("\"");
            stringBuilder.append(parameters.getTableSchema());
            stringBuilder.append("\"");
            stringBuilder.append(".");
        }
        stringBuilder.append("\"");
        stringBuilder.append(parameters.getTableName());
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }

    protected String prepareDropScript(IsRecordBelongsToCurrentTenantConstraintProducerParameters parameters)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ALTER TABLE ");
        stringBuilder.append(prepareTableReference(parameters));
        stringBuilder.append(" DROP CONSTRAINT IF EXISTS ");
        stringBuilder.append(parameters.getConstraintName());
        stringBuilder.append(";");
        return stringBuilder.toString();
    }
}
