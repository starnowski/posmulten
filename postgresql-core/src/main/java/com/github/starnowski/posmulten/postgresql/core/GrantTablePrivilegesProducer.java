package com.github.starnowski.posmulten.postgresql.core;

import java.util.Iterator;
import java.util.List;

public class GrantTablePrivilegesProducer {

    public String produce(String schema, String table, String user, List<String> privileges) {
        StringBuilder sb = new StringBuilder();
        sb.append("GRANT ");
        Iterator<String> it = privileges.iterator();
        while (it.hasNext()){
            sb.append(it.next());
            if (it.hasNext())
            {
                sb.append(", ");
            }
        }
        sb.append(" ON ");
        if (schema != null && !schema.trim().isEmpty()) {
            sb.append(schema);
            sb.append(".");
        }
        sb.append("\"");
        sb.append(table);
        sb.append("\"");
        sb.append(" TO \"");
        sb.append(user);
        sb.append("\";");
        return sb.toString();
    }
}
