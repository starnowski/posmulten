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
package com.github.starnowski.posmulten.postgresql.core.rls;

public class DefaultRLSPolicyProducerParameters implements RLSPolicyProducerParameters{

    private final String policyName;
    private final String policyTable;
    private final String policySchema;
    private final String grantee;
    private final String tenantIdColumn;
    private final PermissionCommandPolicyEnum permissionCommandPolicy;
    private final TenantHasAuthoritiesFunctionInvocationFactory withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory;
    private final TenantHasAuthoritiesFunctionInvocationFactory usingExpressionTenantHasAuthoritiesFunctionInvocationFactory;

    public DefaultRLSPolicyProducerParameters(String policyName, String policyTable, String policySchema, String grantee, String tenantIdColumn, PermissionCommandPolicyEnum permissionCommandPolicy, TenantHasAuthoritiesFunctionInvocationFactory withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory, TenantHasAuthoritiesFunctionInvocationFactory usingExpressionTenantHasAuthoritiesFunctionInvocationFactory) {
        this.policyName = policyName;
        this.policyTable = policyTable;
        this.policySchema = policySchema;
        this.grantee = grantee;
        this.tenantIdColumn = tenantIdColumn;
        this.permissionCommandPolicy = permissionCommandPolicy;
        this.withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory = withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory;
        this.usingExpressionTenantHasAuthoritiesFunctionInvocationFactory = usingExpressionTenantHasAuthoritiesFunctionInvocationFactory;
    }

    @Override
    public String getPolicyName() {
        return policyName;
    }

    @Override
    public String getPolicyTable() {
        return policyTable;
    }

    @Override
    public String getPolicySchema() {
        return policySchema;
    }

    @Override
    public String getGrantee() {
        return grantee;
    }

    @Override
    public String getTenantIdColumn() {
        return tenantIdColumn;
    }

    @Override
    public PermissionCommandPolicyEnum getPermissionCommandPolicy() {
        return permissionCommandPolicy;
    }

    @Override
    public TenantHasAuthoritiesFunctionInvocationFactory getWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory() {
        return withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory;
    }

    @Override
    public TenantHasAuthoritiesFunctionInvocationFactory getUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory() {
        return usingExpressionTenantHasAuthoritiesFunctionInvocationFactory;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private String policyName;
        private String policyTable;
        private String policySchema;
        private String grantee;
        private String tenantIdColumn;
        private PermissionCommandPolicyEnum permissionCommandPolicy;
        private TenantHasAuthoritiesFunctionInvocationFactory withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory;
        private TenantHasAuthoritiesFunctionInvocationFactory usingExpressionTenantHasAuthoritiesFunctionInvocationFactory;

        public DefaultRLSPolicyProducerParameters build()
        {
            return new DefaultRLSPolicyProducerParameters(policyName, policyTable, policySchema, grantee, tenantIdColumn, permissionCommandPolicy, withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory, usingExpressionTenantHasAuthoritiesFunctionInvocationFactory);
        }

        public Builder withPolicyName(String policyName) {
            this.policyName = policyName;
            return this;
        }

        public Builder withPolicyTable(String policyTable) {
            this.policyTable = policyTable;
            return this;
        }

        public Builder withPolicySchema(String policySchema) {
            this.policySchema = policySchema;
            return this;
        }

        public Builder withGrantee(String grantee) {
            this.grantee = grantee;
            return this;
        }

        public Builder withTenantIdColumn(String tenantIdColumn) {
            this.tenantIdColumn = tenantIdColumn;
            return this;
        }

        public Builder withPermissionCommandPolicy(PermissionCommandPolicyEnum permissionCommandPolicy) {
            this.permissionCommandPolicy = permissionCommandPolicy;
            return this;
        }

        public Builder withWithCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory(TenantHasAuthoritiesFunctionInvocationFactory withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory) {
            this.withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory = withCheckExpressionTenantHasAuthoritiesFunctionInvocationFactory;
            return this;
        }

        public Builder withUsingExpressionTenantHasAuthoritiesFunctionInvocationFactory(TenantHasAuthoritiesFunctionInvocationFactory usingExpressionTenantHasAuthoritiesFunctionInvocationFactory) {
            this.usingExpressionTenantHasAuthoritiesFunctionInvocationFactory = usingExpressionTenantHasAuthoritiesFunctionInvocationFactory;
            return this;
        }


    }
}
