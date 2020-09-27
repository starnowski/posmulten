package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import spock.lang.Specification

class IsTenantIdentifierValidConstraintEnricherTest extends Specification {

    static class IsTenantIdentifierValidConstraintProducerKey
    {
        private final String constraintName
        private final String tableName
        private final String tableSchema
        private final String tenantColumnName

        String getConstraintName() {
            return constraintName
        }

        String getTableName() {
            return tableName
        }

        String getTableSchema() {
            return tableSchema
        }

        String getTenantColumnName() {
            return tenantColumnName
        }
        IsTenantIdentifierValidConstraintProducerKey(String constraintName, String tableName, String tableSchema, String tenantColumnName) {
            this.constraintName = constraintName
            this.tableName = tableName
            this.tableSchema = tableSchema
            this.tenantColumnName = tenantColumnName
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            IsTenantIdentifierValidConstraintProducerKey that = (IsTenantIdentifierValidConstraintProducerKey) o

            if (constraintName != that.constraintName) return false
            if (tableName != that.tableName) return false
            if (tableSchema != that.tableSchema) return false
            if (tenantColumnName != that.tenantColumnName) return false

            return true
        }

        int hashCode() {
            int result
            result = (constraintName != null ? constraintName.hashCode() : 0)
            result = 31 * result + (tableName != null ? tableName.hashCode() : 0)
            result = 31 * result + (tableSchema != null ? tableSchema.hashCode() : 0)
            result = 31 * result + (tenantColumnName != null ? tenantColumnName.hashCode() : 0)
            return result
        }
    }
}
