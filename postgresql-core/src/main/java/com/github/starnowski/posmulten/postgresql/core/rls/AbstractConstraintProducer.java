package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

public abstract class AbstractConstraintProducer<P extends IConstraintProducerParameters> {

    public SQLDefinition produce(P parameters)
    {
        validate(parameters);
        return new DefaultSQLDefinition(prepareCreateScript(parameters), prepareDropScript(parameters));
    }

    protected String prepareCreateScript(P parameters)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ALTER TABLE ");
        stringBuilder.append(prepareTableReference(parameters));
        stringBuilder.append(" ADD CONSTRAINT ");
        stringBuilder.append(parameters.getConstraintName());
        stringBuilder.append(" CHECK ");
        stringBuilder.append("(");
        stringBuilder.append(prepareConstraintBody(parameters));
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    protected abstract String prepareConstraintBody(P parameters);

    protected void validate(P parameters)
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
    }

    private String prepareTableReference(P parameters)
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

    protected String prepareDropScript(P parameters)
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
