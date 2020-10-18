package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.rls.IIsTenantIdentifierValidConstraintProducerParameters
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
            List<IIsTenantIdentifierValidConstraintProducerParameters> capturedParameters = new ArrayList<>()

        when:
            def result = builder.build()

        then:
            tableNameTenantNamePairs.size() * producer.produce(_) >>  {
                parameters ->
                    capturedParameters.add(parameters[0])
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().size() == tableNameTenantNamePairs.size()

        where:
            //TODO
            schema          |   defaultValue    |   tableNameTenantNamePairs                                        ||  expectedPassedParameters
            null            |   "some_fun(1)"   |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("leads", "tenant_id", "some_fun(1)", null), key("tenant_identifier_valid", "users", "some_fun(1)", null)]
            "public"        |   "def_fun()"     |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("leads", "tenant_id", "def_fun()", "public"), key("tenant_identifier_valid", "users", "def_fun()", "public")]
            "some_schema"   |   "CONST"         |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("leads", "tenant_id", "CONST", "some_schema"), key("tenant_identifier_valid", "users", "CONST", "some_schema")]
    }

    static SetDefaultStatementProducerParametersKey key(String table, String column, String defaultValueDefinition, String schema)
    {
        new SetDefaultStatementProducerParametersKey(table, column, defaultValueDefinition, schema)
    }

    static class SetDefaultStatementProducerParametersKey {

        private final String table
        private final String column
        private final String defaultValueDefinition
        private final String schema

        SetDefaultStatementProducerParametersKey(String table, String column, String defaultValueDefinition, String schema) {
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
