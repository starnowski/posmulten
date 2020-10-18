package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import javafx.util.Pair
import spock.lang.Specification
import spock.lang.Unroll

class DefaultValueForTenantColumnEnricherTest extends Specification {

    @Unroll
    def "should create sql definitions for all tables" ()
    {
        given:
            def builder = (new DefaultSharedSchemaContextBuilder(schema))
                    .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true)
            for (Pair tableNameTenantNamePair : tableNameTenantNamePairs)
            {
                builder.createRLSPolicyForTable(tableNameTenantNamePair.key, [:], tableNameTenantNamePair.value, null)
            }

        when:
            def result = builder.build()

        then:
            false

        where:
            //TODO
            schema          |   tableNameTenantNamePairs                                        ||  expectedPassedParameters
            null            |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [tp("tenant_identifier_valid", "leads", null, "t_xxx"), tp("tenant_identifier_valid", "users", null, "tenant_id")]
            "public"        |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [tp("tenant_identifier_valid", "leads", "public", "t_xxx"), tp("tenant_identifier_valid", "users", "public", "tenant_id")]
            "some_schema"   |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [tp("tenant_identifier_valid", "leads", "some_schema", "t_xxx"), tp("tenant_identifier_valid", "users", "some_schema", "tenant_id")]
    }

    static class SetDefaultStatementProducerParameters {

        private final String table
        private final String column
        private final String defaultValueDefinition
        private final String schema

        SetDefaultStatementProducerParameters(String table, String column, String defaultValueDefinition, String schema) {
            this.table = table
            this.column = column
            this.defaultValueDefinition = defaultValueDefinition
            this.schema = schema
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
