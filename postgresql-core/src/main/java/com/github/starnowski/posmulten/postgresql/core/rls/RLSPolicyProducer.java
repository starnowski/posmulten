/**
 * Posmulten library is an open-source project for the generation
 * of SQL DDL statements that make it easy for implementation of
 * Shared Schema Multi-tenancy strategy via the Row Security
 * Policies in the Postgres database.
 * <p>
 * Copyright (C) 2020  Szymon Tarnowski
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package com.github.starnowski.posmulten.postgresql.core.rls;

import com.github.starnowski.posmulten.postgresql.core.common.DefaultSQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;

import java.util.Arrays;
import java.util.List;

import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forReference;
import static com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue.forString;
import static com.github.starnowski.posmulten.postgresql.core.rls.PermissionCommandPolicyEnum.*;
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.USING;
import static com.github.starnowski.posmulten.postgresql.core.rls.RLSExpressionTypeEnum.WITH_CHECK;
import static java.lang.String.format;

public class RLSPolicyProducer {

    public SQLDefinition produce(RLSPolicyProducerParameters parameters) {
        validate(parameters);
        return new DefaultSQLDefinition(prepareCreateScript(parameters), prepareDropScript(parameters), prepareCheckingStatements(parameters));
    }

    protected void validate(RLSPolicyProducerParameters parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters object cannot be null");
        }
        if (parameters.getPolicyName() == null) {
            throw new IllegalArgumentException("Policy name cannot be null");
        }
        if (parameters.getPolicyName().trim().isEmpty()) {
            throw new IllegalArgumentException("Policy name cannot be blank");
        }
        if (parameters.getPolicyTable() == null) {
            throw new IllegalArgumentException("Policy table cannot be null");
        }
        if (parameters.getPolicyTable().trim().isEmpty()) {
            throw new IllegalArgumentException("Policy table cannot be blank");
        }
        if (parameters.getPolicySchema() != null && parameters.getPolicySchema().trim().isEmpty()) {
            throw new IllegalArgumentException("Policy schema cannot be blank");
        }
        if (parameters.getGrantee() == null) {
            throw new IllegalArgumentException("Grantee cannot be null");
        }
        if (parameters.getGrantee() != null && parameters.getGrantee().trim().isEmpty()) {
            throw new IllegalArgumentException("Grantee cannot be blank");
        }
        if (parameters.getTenantIdColumn() != null && parameters.getTenantIdColumn().trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant id column cannot be blank");
        }
        if (parameters.getPermissionCommandPolicy() == null) {
            throw new IllegalArgumentException("Permission command policy cannot be null");
        }
        if (parameters.getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory() == null && parameters.getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory() == null) {
            throw new IllegalArgumentException("The components for the USING and the CHECK WITH expressions cannot be null");
        }
        if (INSERT.equals(parameters.getPermissionCommandPolicy()) && parameters.getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory() == null) {
            throw new IllegalArgumentException("For the INSERT permission command the CHECK WITH expressions cannot be null");
        }
        if ((SELECT.equals(parameters.getPermissionCommandPolicy()) || DELETE.equals(parameters.getPermissionCommandPolicy())) && parameters.getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory() == null) {
            throw new IllegalArgumentException("For the SELECT and DELETE permission command the USING expressions cannot be null");
        }
    }

    private String prepareDropScript(RLSPolicyProducerParameters parameters) {
        String tableReference = prepareTableReference(parameters);
        return format("DROP POLICY IF EXISTS %1$s ON %2$s;", parameters.getPolicyName(), tableReference);
    }

    private String prepareTableReference(RLSPolicyProducerParameters parameters) {
        return parameters.getPolicySchema() == null ? parameters.getPolicyTable() : parameters.getPolicySchema() + "." + parameters.getPolicyTable();
    }

    private String prepareCreateScript(RLSPolicyProducerParameters parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE POLICY ");
        sb.append(parameters.getPolicyName());
        sb.append(" ON ");
        if (parameters.getPolicySchema() != null) {
            sb.append(parameters.getPolicySchema());
            sb.append(".");
        }
        sb.append(parameters.getPolicyTable());
        sb.append("\n");
        sb.append("FOR ");
        sb.append(parameters.getPermissionCommandPolicy());
        sb.append("\n");
        sb.append("TO ");
        sb.append("\"");
        sb.append(parameters.getGrantee());
        sb.append("\"");
        if (isUsingExpressionShouldBeApplied(parameters)) {
            sb.append("\n");
            sb.append("USING ");
            sb.append("(");
            sb.append(prepareUsingRLSExpression(parameters));
            sb.append(")");
        }
        if (isCheckWithExpressionShouldBeApplied(parameters)) {
            sb.append("\n");
            sb.append("WITH CHECK ");
            sb.append("(");
            sb.append(prepareWithCheckRLSExpression(parameters));
            sb.append(")");
        }
        sb.append(";");
        return sb.toString();
    }

    protected List<String> prepareCheckingStatements(RLSPolicyProducerParameters parameters) {
        return Arrays.asList(prepareStatementThatChecksIfPolicyExists(parameters), prepareStatementThatChecksIfPolicyForGranteeExists(parameters), prepareStatementThatChecksIfPolicyWithSpecifiedCmdExists(parameters));
    }

    private String prepareStatementThatChecksIfPolicyExists(RLSPolicyProducerParameters parameters) {
        String name = parameters.getPolicyName();
        String table = parameters.getPolicyTable();
        String schema = parameters.getPolicySchema();
        String schemaName = schema == null ? "public" : schema;
        return format("SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = '%1$s' AND pc.relname = '%2$s' AND pn.nspname = '%3$s'", name, table, schemaName);
    }

    private String prepareStatementThatChecksIfPolicyWithSpecifiedCmdExists(RLSPolicyProducerParameters parameters) {
        String name = parameters.getPolicyName();
        String table = parameters.getPolicyTable();
        String schema = parameters.getPolicySchema();
        PermissionCommandPolicyEnum permissionCommandPolicy = parameters.getPermissionCommandPolicy();
        String schemaName = schema == null ? "public" : schema;
        String cmd = null;
        switch (permissionCommandPolicy) {
            case ALL:
                cmd = "*";
                break;
            case SELECT:
                cmd = "r";
                break;
            case INSERT:
                cmd = "a";
                break;
            case UPDATE:
                cmd = "w";
                break;
            case DELETE:
                cmd = "d";
        }
        return format("SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = '%1$s' AND pc.relname = '%2$s' AND pn.nspname = '%3$s' AND pg.polcmd = '%4$s'", name, table, schemaName, cmd);
    }

    private String prepareStatementThatChecksIfPolicyForGranteeExists(RLSPolicyProducerParameters parameters) {
        String name = parameters.getPolicyName();
        String table = parameters.getPolicyTable();
        String schema = parameters.getPolicySchema();
        String grantee = parameters.getGrantee();
        String schemaName = schema == null ? "public" : schema;
        return format("SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn, pg_roles ro WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = '%1$s' AND pc.relname = '%2$s' AND pn.nspname = '%3$s' AND ro.oid = ANY(pg.polroles) AND ro.rolname = '%4$s'", name, table, schemaName, grantee);
    }

    private boolean isCheckWithExpressionShouldBeApplied(RLSPolicyProducerParameters parameters) {
        return !SELECT.equals(parameters.getPermissionCommandPolicy()) && !DELETE.equals(parameters.getPermissionCommandPolicy());
    }

    private boolean isUsingExpressionShouldBeApplied(RLSPolicyProducerParameters parameters) {
        return !INSERT.equals(parameters.getPermissionCommandPolicy());
    }

    private String prepareWithCheckRLSExpression(RLSPolicyProducerParameters parameters) {
        return parameters.getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory().returnTenantHasAuthoritiesFunctionInvocation(prepareTenantIdColumnReference(parameters), parameters.getPermissionCommandPolicy(), WITH_CHECK, forString(parameters.getPolicyTable()), preparePolicySchemaFunctionArgument(parameters.getPolicySchema()));
    }

    private FunctionArgumentValue preparePolicySchemaFunctionArgument(String schema) {
        return schema == null ? forString("public") : forString(schema);
    }

    private String prepareUsingRLSExpression(RLSPolicyProducerParameters parameters) {
        return parameters.getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory().returnTenantHasAuthoritiesFunctionInvocation(prepareTenantIdColumnReference(parameters), parameters.getPermissionCommandPolicy(), USING, forString(parameters.getPolicyTable()), preparePolicySchemaFunctionArgument(parameters.getPolicySchema()));
    }

    private FunctionArgumentValue prepareTenantIdColumnReference(RLSPolicyProducerParameters parameters) {
        return forReference(parameters.getTenantIdColumn() == null ? "tenant_id" : parameters.getTenantIdColumn());
    }
}
