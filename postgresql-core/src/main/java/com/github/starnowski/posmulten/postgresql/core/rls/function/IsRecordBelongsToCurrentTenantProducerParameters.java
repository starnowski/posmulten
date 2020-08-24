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

import com.github.starnowski.posmulten.postgresql.core.common.function.IFunctionArgument;
import javafx.util.Pair;

import java.util.List;

public class IsRecordBelongsToCurrentTenantProducerParameters implements AbstractIsRecordBelongsToCurrentTenantProducerParameters {

    private final String functionName;
    private final String schema;
    private final List<Pair<String, IFunctionArgument>> keyColumnsPairsList;
    private final String tenantColumn;
    private final String recordTableName;
    private final String recordSchemaName;
    private final IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory;

    public IsRecordBelongsToCurrentTenantProducerParameters(String functionName, String schema, List<Pair<String, IFunctionArgument>> keyColumnsPairsList, String tenantColumn, String recordTableName, String recordSchemaName, IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory) {
        this.functionName = functionName;
        this.schema = schema;
        this.keyColumnsPairsList = keyColumnsPairsList;
        this.tenantColumn = tenantColumn;
        this.recordTableName = recordTableName;
        this.recordSchemaName = recordSchemaName;
        this.iGetCurrentTenantIdFunctionInvocationFactory = iGetCurrentTenantIdFunctionInvocationFactory;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public List<Pair<String, IFunctionArgument>> getKeyColumnsPairsList() {
        return keyColumnsPairsList;
    }

    @Override
    public String getTenantColumn() {
        return tenantColumn;
    }

    @Override
    public String getRecordTableName() {
        return recordTableName;
    }

    @Override
    public String getRecordSchemaName() {
        return recordSchemaName;
    }

    public IGetCurrentTenantIdFunctionInvocationFactory getIGetCurrentTenantIdFunctionInvocationFactory() {
        return iGetCurrentTenantIdFunctionInvocationFactory;
    }

    public static class Builder
    {
        private String functionName;
        private String schema;
        private List<Pair<String, IFunctionArgument>> keyColumnsPairsList;
        private String tenantColumn;
        private String recordTableName;
        private String recordSchemaName;
        private IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory;

        public Builder withFunctionName(String functionName) {
            this.functionName = functionName;
            return this;
        }

        public Builder withSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public Builder withKeyColumnsPairsList(List<Pair<String, IFunctionArgument>> keyColumnsPairsList) {
            this.keyColumnsPairsList = keyColumnsPairsList;
            return this;
        }

        public Builder withTenantColumn(String tenantColumn) {
            this.tenantColumn = tenantColumn;
            return this;
        }

        public Builder withRecordTableName(String recordTableName) {
            this.recordTableName = recordTableName;
            return this;
        }

        public Builder withRecordSchemaName(String recordSchemaName) {
            this.recordSchemaName = recordSchemaName;
            return this;
        }

        public Builder withiGetCurrentTenantIdFunctionInvocationFactory(IGetCurrentTenantIdFunctionInvocationFactory iGetCurrentTenantIdFunctionInvocationFactory) {
            this.iGetCurrentTenantIdFunctionInvocationFactory = iGetCurrentTenantIdFunctionInvocationFactory;
            return this;
        }

        public IsRecordBelongsToCurrentTenantProducerParameters build()
        {
            return new IsRecordBelongsToCurrentTenantProducerParameters(functionName, schema, keyColumnsPairsList, tenantColumn, recordTableName, recordSchemaName, iGetCurrentTenantIdFunctionInvocationFactory);
        }
    }
}
