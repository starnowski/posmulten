package com.github.starnowski.posmulten.postgresql.core;

public class GrantSequencePrivilegesProducer {

    public String produce(String schema, String sequence, String user) {
        validateParameters(schema, sequence, user);
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
        if (user.trim().isEmpty())
        {
            throw new IllegalArgumentException("user cannot be blank");
        }
    }
}
