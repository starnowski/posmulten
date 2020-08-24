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
package com.github.starnowski.posmulten.postgresql.core.context;

import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory;

import java.util.Objects;

public class TableRLSPolicySQLDefinitionsProducerParameters implements AbstractTableRLSPolicySQLDefinitionsProducerParameters{

    private final String grantee;

    private final TableKey tableKey;

    private final String policyName;
    private final TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory;
    private final String tenantIdColumn;
    private final String defaultTenantIdColumn;
    public TableRLSPolicySQLDefinitionsProducerParameters(String grantee, TableKey tableKey, String policyName, TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory, String tenantIdColumn, String defaultTenantIdColumn) {
        this.grantee = grantee;
        this.tableKey = tableKey;
        this.policyName = policyName;
        this.tenantHasAuthoritiesFunctionInvocationFactory = tenantHasAuthoritiesFunctionInvocationFactory;
        this.tenantIdColumn = tenantIdColumn;
        this.defaultTenantIdColumn = defaultTenantIdColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableRLSPolicySQLDefinitionsProducerParameters that = (TableRLSPolicySQLDefinitionsProducerParameters) o;
        return Objects.equals(grantee, that.grantee) &&
                Objects.equals(tableKey, that.tableKey) &&
                Objects.equals(policyName, that.policyName) &&
                Objects.equals(tenantHasAuthoritiesFunctionInvocationFactory, that.tenantHasAuthoritiesFunctionInvocationFactory) &&
                Objects.equals(tenantIdColumn, that.tenantIdColumn) &&
                Objects.equals(defaultTenantIdColumn, that.defaultTenantIdColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grantee, tableKey, policyName, tenantHasAuthoritiesFunctionInvocationFactory, tenantIdColumn, defaultTenantIdColumn);
    }

    public String getGrantee() {
        return grantee;
    }

    public TableKey getTableKey() {
        return tableKey;
    }

    public String getPolicyName() {
        return policyName;
    }

    public TenantHasAuthoritiesFunctionInvocationFactory getTenantHasAuthoritiesFunctionInvocationFactory() {
        return tenantHasAuthoritiesFunctionInvocationFactory;
    }

    @Override
    public String getTenantIdColumn() {
        return tenantIdColumn;
    }

    public String getDefaultTenantIdColumn() {
        return defaultTenantIdColumn;
    }

    public static class TableRLSPolicySQLDefinitionsProducerParametersBuilder
    {
        private String grantee;
        private TableKey tableKey;
        private String policyName;
        private TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory;
        private String tenantIdColumn;
        private String defaultTenantIdColumn;

        public TableRLSPolicySQLDefinitionsProducerParametersBuilder withGrantee(String grantee) {
            this.grantee = grantee;
            return this;
        }

        public TableRLSPolicySQLDefinitionsProducerParametersBuilder withTableKey(TableKey tableKey) {
            this.tableKey = tableKey;
            return this;
        }

        public TableRLSPolicySQLDefinitionsProducerParametersBuilder withPolicyName(String policyName) {
            this.policyName = policyName;
            return this;
        }

        public TableRLSPolicySQLDefinitionsProducerParametersBuilder withTenantHasAuthoritiesFunctionInvocationFactory(TenantHasAuthoritiesFunctionInvocationFactory tenantHasAuthoritiesFunctionInvocationFactory) {
            this.tenantHasAuthoritiesFunctionInvocationFactory = tenantHasAuthoritiesFunctionInvocationFactory;
            return this;
        }

        public TableRLSPolicySQLDefinitionsProducerParametersBuilder withTenantIdColumn(String tenantIdColumn) {
            this.tenantIdColumn = tenantIdColumn;
            return this;
        }

        public TableRLSPolicySQLDefinitionsProducerParametersBuilder withDefaultTenantIdColumn(String defaultTenantIdColumn) {
            this.defaultTenantIdColumn = defaultTenantIdColumn;
            return this;
        }

        public TableRLSPolicySQLDefinitionsProducerParameters build()
        {
            return new TableRLSPolicySQLDefinitionsProducerParameters(grantee, tableKey, policyName, tenantHasAuthoritiesFunctionInvocationFactory, tenantIdColumn, defaultTenantIdColumn);
        }
    }
}
