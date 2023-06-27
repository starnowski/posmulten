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

import com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidFunctionInvocationFactory;

public class DefaultIsTenantIdentifierValidConstraintProducerParameters implements IIsTenantIdentifierValidConstraintProducerParameters{

    private final String constraintName;
    private final String tableName;
    private final String tableSchema;
    private final String tenantColumnName;
    private final IIsTenantValidFunctionInvocationFactory iIsTenantValidFunctionInvocationFactory;

    public DefaultIsTenantIdentifierValidConstraintProducerParameters(String constraintName, String tableName, String tableSchema, String tenantColumnName, IIsTenantValidFunctionInvocationFactory iIsTenantValidFunctionInvocationFactory) {
        this.constraintName = constraintName;
        this.tableName = tableName;
        this.tableSchema = tableSchema;
        this.tenantColumnName = tenantColumnName;
        this.iIsTenantValidFunctionInvocationFactory = iIsTenantValidFunctionInvocationFactory;
    }

    @Override
    public String getConstraintName() {
        return constraintName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getTableSchema() {
        return tableSchema;
    }

    @Override
    public String getTenantColumnName() {
        return tenantColumnName;
    }

    @Override
    public IIsTenantValidFunctionInvocationFactory getIIsTenantValidFunctionInvocationFactory() {
        return iIsTenantValidFunctionInvocationFactory;
    }

    public static DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder builder()
    {
        return new DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder();
    }

    public static class DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder
    {
        private String constraintName;
        private String tableName;
        private String tableSchema;
        private String tenantColumnName;
        private IIsTenantValidFunctionInvocationFactory iIsTenantValidFunctionInvocationFactory;

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withConstraintName(String constraintName) {
            this.constraintName = constraintName;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withTableSchema(String tableSchema) {
            this.tableSchema = tableSchema;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withTenantColumnName(String tenantColumnName) {
            this.tenantColumnName = tenantColumnName;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParametersBuilder withIIsTenantValidFunctionInvocationFactory(IIsTenantValidFunctionInvocationFactory iIsTenantValidFunctionInvocationFactory) {
            this.iIsTenantValidFunctionInvocationFactory = iIsTenantValidFunctionInvocationFactory;
            return this;
        }

        public DefaultIsTenantIdentifierValidConstraintProducerParameters build()
        {
            return new DefaultIsTenantIdentifierValidConstraintProducerParameters(constraintName, tableName, tableSchema, tenantColumnName, iIsTenantValidFunctionInvocationFactory);
        }
    }
}
