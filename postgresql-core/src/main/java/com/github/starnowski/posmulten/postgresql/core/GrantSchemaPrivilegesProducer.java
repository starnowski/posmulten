package com.github.starnowski.posmulten.postgresql.core;

import java.util.Iterator;
import java.util.List;

public class GrantSchemaPrivilegesProducer {
    public String produce(String schema, String policyTargetUsername, List<String> privileges) {
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
        sb.append(" ON SCHEMA ");
        sb.append(schema);
        sb.append(" TO \"");
        sb.append(policyTargetUsername);
        sb.append("\";");
        return sb.toString();
    }
}
