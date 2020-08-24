/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posmulten.postgresql.core.rls.function;

public class TenantHasAuthoritiesFunctionProducerParameters implements ITenantHasAuthoritiesFunctionProducerParameters{

    private final String functionName;
    private final String schema;
    private final EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory;
    private final String tenantIdArgumentType;
    private final String permissionCommandPolicyArgumentType;
    private final String rlsExpressionArgumentType;
    private final String tableArgumentType;
    private final String schemaArgumentType;

    public TenantHasAuthoritiesFunctionProducerParameters(String functionName, String schema, EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory) {
        this(functionName, schema, equalsCurrentTenantIdentifierFunctionInvocationFactory, null, null, null, null, null);
    }

    public TenantHasAuthoritiesFunctionProducerParameters(String functionName, String schema, EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory, String tenantIdArgumentType, String permissionCommandPolicyArgumentType, String rlsExpressionArgumentType, String tableArgumentType, String schemaArgumentType) {
        this.functionName = functionName;
        this.schema = schema;
        this.equalsCurrentTenantIdentifierFunctionInvocationFactory = equalsCurrentTenantIdentifierFunctionInvocationFactory;
        this.tenantIdArgumentType = tenantIdArgumentType;
        this.permissionCommandPolicyArgumentType = permissionCommandPolicyArgumentType;
        this.rlsExpressionArgumentType = rlsExpressionArgumentType;
        this.tableArgumentType = tableArgumentType;
        this.schemaArgumentType = schemaArgumentType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getSchema() {
        return schema;
    }

    public String getTenantIdArgumentType() {
        return tenantIdArgumentType;
    }

    public String getPermissionCommandPolicyArgumentType() {
        return permissionCommandPolicyArgumentType;
    }

    public String getRLSExpressionArgumentType() {
        return rlsExpressionArgumentType;
    }

    public String getTableArgumentType() {
        return tableArgumentType;
    }

    public String getSchemaArgumentType() {
        return schemaArgumentType;
    }

    public EqualsCurrentTenantIdentifierFunctionInvocationFactory getEqualsCurrentTenantIdentifierFunctionInvocationFactory() {
        return equalsCurrentTenantIdentifierFunctionInvocationFactory;
    }

    @Override
    public String toString() {
        return "TenantHasAuthoritiesFunctionProducerParameters{" +
                "functionName='" + functionName + '\'' +
                ", schema='" + schema + '\'' +
                ", tenantIdArgumentType='" + tenantIdArgumentType + '\'' +
                ", permissionCommandPolicyArgumentType='" + permissionCommandPolicyArgumentType + '\'' +
                ", rlsExpressionArgumentType='" + rlsExpressionArgumentType + '\'' +
                ", tableArgumentType='" + tableArgumentType + '\'' +
                ", schemaArgumentType='" + schemaArgumentType + '\'' +
                '}';
    }

    public static class TenantHasAuthoritiesFunctionProducerParametersBuilder
    {
        private String functionName;
        private String schema;
        private EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory;
        private String tenantIdArgumentType;
        private String permissionCommandPolicyArgumentType;
        private String rlsExpressionArgumentType;
        private String tableArgumentType;
        private String schemaArgumentType;

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withFunctionName(String functionName) {
            this.functionName = functionName;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withEqualsCurrentTenantIdentifierFunctionInvocationFactory(EqualsCurrentTenantIdentifierFunctionInvocationFactory equalsCurrentTenantIdentifierFunctionInvocationFactory) {
            this.equalsCurrentTenantIdentifierFunctionInvocationFactory = equalsCurrentTenantIdentifierFunctionInvocationFactory;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withTenantIdArgumentType(String tenantIdArgumentType) {
            this.tenantIdArgumentType = tenantIdArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withPermissionCommandPolicyArgumentType(String permissionCommandPolicyArgumentType) {
            this.permissionCommandPolicyArgumentType = permissionCommandPolicyArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withRlsExpressionArgumentType(String rlsExpressionArgumentType) {
            this.rlsExpressionArgumentType = rlsExpressionArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withTableArgumentType(String tableArgumentType) {
            this.tableArgumentType = tableArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParametersBuilder withSchemaArgumentType(String schemaArgumentType) {
            this.schemaArgumentType = schemaArgumentType;
            return this;
        }

        public TenantHasAuthoritiesFunctionProducerParameters build()
        {
            return new TenantHasAuthoritiesFunctionProducerParameters(functionName, schema, equalsCurrentTenantIdentifierFunctionInvocationFactory, tenantIdArgumentType, permissionCommandPolicyArgumentType, rlsExpressionArgumentType, tableArgumentType, schemaArgumentType);
        }
    }
}
