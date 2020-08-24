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

import com.github.starnowski.posmulten.postgresql.core.common.function.FunctionArgumentValue;
import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public final class DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters implements IsRecordBelongsToCurrentTenantConstraintProducerParameters{

    private final String constraintName;
    private final String tableName;
    private final String tableSchema;
    private final Map<String, FunctionArgumentValue> primaryColumnsValuesMap;
    private final IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;

    public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters(String constraintName, String tableName, String tableSchema, Map<String, FunctionArgumentValue> primaryColumnsValuesMap, IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory) {
        this.constraintName = constraintName;
        this.tableName = tableName;
        this.tableSchema = tableSchema;
        this.primaryColumnsValuesMap = unmodifiableMap(primaryColumnsValuesMap);
        this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public Map<String, FunctionArgumentValue> getPrimaryColumnsValuesMap() {
        return primaryColumnsValuesMap;
    }

    public IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() {
        return isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    }

    public static DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder builder()
    {
        return new DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder();
    }

    public static class DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder
    {
        private String constraintName;
        private String tableName;
        private String tableSchema;
        private Map<String, FunctionArgumentValue> primaryColumnsValuesMap;
        private IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withConstraintName(String constraintName) {
            this.constraintName = constraintName;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withTableSchema(String tableSchema) {
            this.tableSchema = tableSchema;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withPrimaryColumnsValuesMap(Map<String, FunctionArgumentValue> primaryColumnsValuesMap) {
            this.primaryColumnsValuesMap = primaryColumnsValuesMap;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParametersBuilder withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory) {
            this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
            return this;
        }

        public DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters build()
        {
            return new DefaultIsRecordBelongsToCurrentTenantConstraintProducerParameters(constraintName, tableName, tableSchema, primaryColumnsValuesMap, isRecordBelongsToCurrentTenantFunctionInvocationFactory);
        }
    }
}
