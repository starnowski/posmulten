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

import com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionInvocationFactory;

import java.util.Map;
import java.util.Objects;

public class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters implements IIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters {

    private final String constraintName;
    private final TableKey tableKey;
    private final IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    private final Map<String, String> foreignKeyPrimaryKeyMappings;

    public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters(String constraintName, TableKey tableKey, IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory, Map<String, String> foreignKeyPrimaryKeyMappings) {
        this.constraintName = constraintName;
        this.tableKey = tableKey;
        this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
        this.foreignKeyPrimaryKeyMappings = foreignKeyPrimaryKeyMappings;
    }

    @Override
    public TableKey getTableKey() {
        return tableKey;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public IsRecordBelongsToCurrentTenantFunctionInvocationFactory getIsRecordBelongsToCurrentTenantFunctionInvocationFactory() {
        return isRecordBelongsToCurrentTenantFunctionInvocationFactory;
    }

    public Map<String, String> getForeignKeyPrimaryKeyMappings() {
        return foreignKeyPrimaryKeyMappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters that = (IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters) o;
        return Objects.equals(constraintName, that.constraintName) &&
                Objects.equals(tableKey, that.tableKey) &&
                Objects.equals(isRecordBelongsToCurrentTenantFunctionInvocationFactory, that.isRecordBelongsToCurrentTenantFunctionInvocationFactory) &&
                Objects.equals(foreignKeyPrimaryKeyMappings, that.foreignKeyPrimaryKeyMappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraintName, tableKey, isRecordBelongsToCurrentTenantFunctionInvocationFactory, foreignKeyPrimaryKeyMappings);
    }

    public static IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder builder()
    {
        return new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder();
    }

    public static class IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder
    {
        private String constraintName;
        private TableKey tableKey;
        private IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory;
        private Map<String, String> foreignKeyPrimaryKeyMappings;

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withConstraintName(String constraintName) {
            this.constraintName = constraintName;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withTableKey(TableKey tableKey) {
            this.tableKey = tableKey;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withIsRecordBelongsToCurrentTenantFunctionInvocationFactory(IsRecordBelongsToCurrentTenantFunctionInvocationFactory isRecordBelongsToCurrentTenantFunctionInvocationFactory) {
            this.isRecordBelongsToCurrentTenantFunctionInvocationFactory = isRecordBelongsToCurrentTenantFunctionInvocationFactory;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParametersBuilder withForeignKeyPrimaryKeyMappings(Map<String, String> foreignKeyPrimaryKeyMappings) {
            this.foreignKeyPrimaryKeyMappings = foreignKeyPrimaryKeyMappings;
            return this;
        }

        public IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters build()
        {
            return new IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducerParameters(constraintName, tableKey, isRecordBelongsToCurrentTenantFunctionInvocationFactory, foreignKeyPrimaryKeyMappings);
        }
    }
}
