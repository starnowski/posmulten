package com.github.starnowski.posmulten.postgresql.core;

public class GrantSequencePrivilegesProducer {

    public String produce(String schema, String sequence, String user) {
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
}
