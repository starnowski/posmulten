package com.github.starnowski.posmulten.postgresql.core;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;

public class GrantSequencePrivilegesProducer {

    public SQLDefinition produce(String schema, String sequence, String user) {
        validateParameters(schema, sequence, user);
        return new DefaultSQLDefinition(prepareCreateScript(schema, sequence, user), prepareDropScript(schema, sequence, user));
    }

    private String prepareDropScript(String schema, String sequence, String user) {
        StringBuilder sb = new StringBuilder();
        sb.append("REVOKE ALL PRIVILEGES ON SEQUENCE ");
        if (schema == null)
        {
            sb.append("public");
        } else {
            sb.append(schema);
        }
        sb.append(".");
        sb.append("\"");
        sb.append(sequence);
        sb.append("\"");
        sb.append(" FROM \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }

    private String prepareCreateScript(String schema, String sequence, String user) {
        StringBuilder sb = new StringBuilder();
        sb.append("GRANT ALL PRIVILEGES ON SEQUENCE ");
        if (schema == null)
        {
            sb.append("public");
        } else {
            sb.append(schema);
        }
        sb.append(".");
        sb.append("\"");
        sb.append(sequence);
        sb.append("\"");
        sb.append(" TO \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }

    private void validateParameters(String schema, String sequence, String user) {
        if (schema != null && schema.trim().isEmpty())
        {
            throw new IllegalArgumentException("schema cannot be blank");
        }
        if (user == null)
        {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (user.trim().isEmpty())
        {
            throw new IllegalArgumentException("user cannot be blank");
        }
        if (sequence == null)
        {
            throw new IllegalArgumentException("sequence cannot be null");
        }
        if (sequence.trim().isEmpty())
        {
            throw new IllegalArgumentException("sequence cannot be blank");
        }
    }
}
