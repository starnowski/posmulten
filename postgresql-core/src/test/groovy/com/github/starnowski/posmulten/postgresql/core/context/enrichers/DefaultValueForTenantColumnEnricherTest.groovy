package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import spock.lang.Specification

class DefaultValueForTenantColumnEnricherTest extends Specification {



    static class SetDefaultStatementProducerParameters {

        private final String table;
        private final String column;
        private final String defaultValueDefinition;
        private final String schema;

        SetDefaultStatementProducerParameters(String table, String column, String defaultValueDefinition, String schema) {
            this.table = table;
            this.column = column;
            this.defaultValueDefinition = defaultValueDefinition;
            this.schema = schema;
        }

        String getTable() {
            table
        }

        String getColumn() {
            column
        }

        String getDefaultValueDefinition() {
            defaultValueDefinition
        }

        String getSchema() {
            schema
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            SetDefaultStatementProducerParameters that = (SetDefaultStatementProducerParameters) o

            if (column != that.column) return false
            if (defaultValueDefinition != that.defaultValueDefinition) return false
            if (schema != that.schema) return false
            if (table != that.table) return false

            return true
        }

        int hashCode() {
            int result
            result = (table != null ? table.hashCode() : 0)
            result = 31 * result + (column != null ? column.hashCode() : 0)
            result = 31 * result + (defaultValueDefinition != null ? defaultValueDefinition.hashCode() : 0)
            result = 31 * result + (schema != null ? schema.hashCode() : 0)
            return result
        }
    }
}
