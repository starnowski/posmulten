package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.ISetDefaultStatementProducerParameters
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
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
            Set<IIsTenantIdentifierValidConstraintProducerParameters> capturedParameters = new HashSet<>()
            SetDefaultStatementProducer producer = Mock(SetDefaultStatementProducer)
            SQLDefinition mockedSQLDefinition = Mock(SQLDefinition)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def tested = new DefaultValueForTenantColumnEnricher(producer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            tableNameTenantNamePairs.size() * producer.produce(_) >>  {
                parameters ->
                    capturedParameters.add(key(parameters[0]))
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().size() == tableNameTenantNamePairs.size()
            capturedParameters == new HashSet(expectedPassedParameters)


        where:
            //TODO
            schema          |   defaultValue    |   tableNameTenantNamePairs                                        ||  expectedPassedParameters
            null            |   "some_fun(1)"   |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("leads", "tenant_id", "some_fun(1)", null), key("users", "t_xxx", "some_fun(1)", null)]
            "public"        |   "def_fun()"     |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("leads", "tenant_id", "def_fun()", "public"), key("users", "t_xxx", "def_fun()", "public")]
            "some_schema"   |   "CONST"         |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("leads", "tenant_id", "CONST", "some_schema"), key("users", "t_xxx", "CONST", "some_schema")]
    }

    static SetDefaultStatementProducerParametersKey key(ISetDefaultStatementProducerParameters parameters)
    {
        new SetDefaultStatementProducerParametersKey(parameters.getTable(), parameters.getColumn(), parameters.getDefaultValueDefinition(), parameters.getSchema())
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

            SetDefaultStatementProducerParametersKey that = (SetDefaultStatementProducerParametersKey) o

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
