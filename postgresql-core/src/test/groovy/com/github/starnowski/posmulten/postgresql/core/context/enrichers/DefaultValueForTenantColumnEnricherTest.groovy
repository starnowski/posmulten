package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.ISetDefaultStatementProducerParameters
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.rls.IIsTenantIdentifierValidConstraintProducerParameters
import com.github.starnowski.posmulten.postgresql.core.rls.function.IGetCurrentTenantIdFunctionInvocationFactory
import javafx.util.Pair
import spock.lang.Specification
import spock.lang.Unroll

class DefaultValueForTenantColumnEnricherTest extends Specification {

    @Unroll
    def "should create sql definitions that add default value declaration '#defaultValue' for schema '#schema' for all tables (#tableNameTenantNamePairs)" ()
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
            IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            getCurrentTenantIdFunctionInvocationFactory.returnGetCurrentTenantIdFunctionInvocation() >> defaultValue
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            context.setIGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
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
            schema          |   defaultValue    |   tableNameTenantNamePairs                                        ||  expectedPassedParameters
            null            |   "some_fun(1)"   |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("users", "tenant_id", "some_fun(1)", null), key("leads", "t_xxx", "some_fun(1)", null)]
            "public"        |   "def_fun()"     |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("users", "tenant_id", "def_fun()", "public"), key("leads", "t_xxx", "def_fun()", "public")]
            "some_schema"   |   "CONST"         |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]    ||  [key("users", "tenant_id", "CONST", "some_schema"), key("leads", "t_xxx", "CONST", "some_schema")]
    }

    @Unroll
    def "should create sql definitions that add default value declaration for schema '#schema' for all tables (#tableNameTenantNamePairs) and sets default tenant column name (#defaultTenantColumn) if tables does not have specified tenant column name" ()
    {
        given:
            def builder = (new DefaultSharedSchemaContextBuilder(schema))
                    .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true)
                    .setDefaultTenantIdColumn(defaultTenantColumn)
            for (Pair tableNameTenantNamePair : tableNameTenantNamePairs)
            {
                builder.createRLSPolicyForTable(tableNameTenantNamePair.key, [:], tableNameTenantNamePair.value, null)
            }
            Set<IIsTenantIdentifierValidConstraintProducerParameters> capturedParameters = new HashSet<>()
            SetDefaultStatementProducer producer = Mock(SetDefaultStatementProducer)
            SQLDefinition mockedSQLDefinition = Mock(SQLDefinition)
            IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            getCurrentTenantIdFunctionInvocationFactory.returnGetCurrentTenantIdFunctionInvocation() >> "some_fun(1)"
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            context.setIGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
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
            schema          |   defaultTenantColumn     |   tableNameTenantNamePairs                                        ||  expectedPassedParameters
            null            |   "default_ten_col"       |   [new Pair("users", "tenant_id"), new Pair("leads", null)]       ||  [key("users", "tenant_id", "some_fun(1)", null), key("leads", "default_ten_col", "some_fun(1)", null)]
            "public"        |   "column_tent"           |   [new Pair("users", null), new Pair("leads", "t_xxx")]           ||  [key("users", "column_tent", "def_fun()", "public"), key("leads", "t_xxx", "def_fun()", "public")]
            "some_schema"   |   "tenant_uuid"           |   [new Pair("users", null), new Pair("leads", null)]              ||  [key("users", "tenant_uuid", "CONST", "some_schema"), key("leads", "tenant_uuid", "CONST", "some_schema")]
    }

    @Unroll
    def "should create sql definitions that add default value declaration '#defaultValue' for schema '#schema' for tables which tenant column should be created (#tablesThatRequiredTenantColumn) amongst tables (#tableNameTenantNamePairs)" ()
    {
        given:
            def builder = (new DefaultSharedSchemaContextBuilder(schema))
            for (Pair tableNameTenantNamePair : tableNameTenantNamePairs)
            {
                builder.createRLSPolicyForTable(tableNameTenantNamePair.key, [:], tableNameTenantNamePair.value, null)
            }
            for (String tableThatRequiredTenantColumn : tablesThatRequiredTenantColumn)
            {
                builder.createTenantColumnForTable(tableThatRequiredTenantColumn)
            }
            Set<IIsTenantIdentifierValidConstraintProducerParameters> capturedParameters = new HashSet<>()
            SetDefaultStatementProducer producer = Mock(SetDefaultStatementProducer)
            SQLDefinition mockedSQLDefinition = Mock(SQLDefinition)
            IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            getCurrentTenantIdFunctionInvocationFactory.returnGetCurrentTenantIdFunctionInvocation() >> defaultValue
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            context.setIGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
            def tested = new DefaultValueForTenantColumnEnricher(producer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            expectedPassedParameters.size() * producer.produce(_) >>  {
                parameters ->
                    capturedParameters.add(key(parameters[0]))
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().size() == expectedPassedParameters.size()
            capturedParameters == new HashSet(expectedPassedParameters)


        where:
            schema          |   defaultValue    |   tableNameTenantNamePairs                                                                        |   tablesThatRequiredTenantColumn  ||  expectedPassedParameters
            null            |   "some_fun(1)"   |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx"), new Pair("comments", "t_xxx")]     |   ["comments"]                    ||  [key("comments", "t_xxx", "some_fun(1)", null)]
            "public"        |   "def_fun()"     |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]                                    |   ["leads"]                       ||  [key("leads", "t_xxx", "def_fun()", "public")]
            "some_schema"   |   "CONST"         |   [new Pair("leads", "tenant_id"), new Pair("users", "t_xxx"), new Pair("posts", "tenant")]       |   ["posts", "users"]              ||  [key("users", "t_xxx", "CONST", "some_schema"), key("posts", "tenant", "CONST", "some_schema")]
    }

    @Unroll
    def "should create sql definitions that add default value declaration '#defaultValue' for schema '#schema' and skip tables  (#tablesThatRequiredTenantColumn) amongst tables (#tableNameTenantNamePairs)" ()
    {
        given:
            def builder = (new DefaultSharedSchemaContextBuilder(schema))
                    .setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true)
            for (Pair tableNameTenantNamePair : tableNameTenantNamePairs)
            {
                builder.createRLSPolicyForTable(tableNameTenantNamePair.key, [:], tableNameTenantNamePair.value, null)
            }
            for (String tableThatShouldBeSkipped : tablesThatShouldBeSkipped)
            {
                builder.skipAddingOfTenantColumnDefaultValueForTable(tableThatShouldBeSkipped)
            }
            Set<IIsTenantIdentifierValidConstraintProducerParameters> capturedParameters = new HashSet<>()
            SetDefaultStatementProducer producer = Mock(SetDefaultStatementProducer)
            SQLDefinition mockedSQLDefinition = Mock(SQLDefinition)
            IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            getCurrentTenantIdFunctionInvocationFactory.returnGetCurrentTenantIdFunctionInvocation() >> defaultValue
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            context.setIGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
            def tested = new DefaultValueForTenantColumnEnricher(producer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            expectedPassedParameters.size() * producer.produce(_) >>  {
                parameters ->
                    capturedParameters.add(key(parameters[0]))
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().size() == expectedPassedParameters.size()
            capturedParameters == new HashSet(expectedPassedParameters)


        where:
            schema          |   defaultValue    |   tableNameTenantNamePairs                                                                        |   tablesThatShouldBeSkipped       ||  expectedPassedParameters
            null            |   "some_fun(1)"   |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx"), new Pair("comments", "t_xxx")]     |   ["users", "leads"]              ||  [key("comments", "t_xxx", "some_fun(1)", null)]
            "public"        |   "def_fun()"     |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx")]                                    |   ["users"]                       ||  [key("leads", "t_xxx", "def_fun()", "public")]
            "some_schema"   |   "CONST"         |   [new Pair("leads", "tenant_id"), new Pair("users", "t_xxx"), new Pair("posts", "tenant")]       |   ["leads"]                       ||  [key("users", "t_xxx", "CONST", "some_schema"), key("posts", "tenant", "CONST", "some_schema")]
    }

    @Unroll
    def "should create sql definitions that add default value declaration '#defaultValue' for schema '#schema' and skip tables  (#tablesThatRequiredTenantColumn) amongst tables (#tableNameTenantNamePairs) with (#tablesThatRequiredTenantColumn) tables that required tenant column creation" ()
    {
        given:
            def builder = (new DefaultSharedSchemaContextBuilder(schema))
            for (Pair tableNameTenantNamePair : tableNameTenantNamePairs)
            {
                builder.createRLSPolicyForTable(tableNameTenantNamePair.key, [:], tableNameTenantNamePair.value, null)
            }
            for (String tableThatShouldBeSkipped : tablesThatShouldBeSkipped)
            {
                builder.skipAddingOfTenantColumnDefaultValueForTable(tableThatShouldBeSkipped)
            }
            for (String tableThatRequiredTenantColumn : tablesThatRequiredTenantColumn)
            {
                builder.createTenantColumnForTable(tableThatRequiredTenantColumn)
            }
            Set<IIsTenantIdentifierValidConstraintProducerParameters> capturedParameters = new HashSet<>()
            SetDefaultStatementProducer producer = Mock(SetDefaultStatementProducer)
            SQLDefinition mockedSQLDefinition = Mock(SQLDefinition)
            IGetCurrentTenantIdFunctionInvocationFactory getCurrentTenantIdFunctionInvocationFactory = Mock(IGetCurrentTenantIdFunctionInvocationFactory)
            getCurrentTenantIdFunctionInvocationFactory.returnGetCurrentTenantIdFunctionInvocation() >> defaultValue
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            context.setIGetCurrentTenantIdFunctionInvocationFactory(getCurrentTenantIdFunctionInvocationFactory)
            def tested = new DefaultValueForTenantColumnEnricher(producer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            expectedPassedParameters.size() * producer.produce(_) >>  {
                parameters ->
                    capturedParameters.add(key(parameters[0]))
                    mockedSQLDefinition
            }
            result.getSqlDefinitions().size() == expectedPassedParameters.size()
            capturedParameters == new HashSet(expectedPassedParameters)


        where:
            schema          |   defaultValue    |   tableNameTenantNamePairs                                                                                                        |   tablesThatShouldBeSkipped       |   tablesThatRequiredTenantColumn      ||  expectedPassedParameters
            null            |   "some_fun(1)"   |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx"), new Pair("comments", "t_xxx"), new Pair("posts", "tenant")]        |   ["users", "leads", "posts"]              |   ["comments", "posts", "users"]      ||  [key("comments", "t_xxx", "some_fun(1)", null)]
            "public"        |   "def_fun()"     |   [new Pair("users", "tenant_id"), new Pair("leads", "t_xxx"), new Pair("posts", "tenant"), new Pair("comments", "t_xxx")]        |   ["users"]                       |   ["leads", "users"]                  ||  [key("leads", "t_xxx", "def_fun()", "public")]
            "some_schema"   |   "CONST"         |   [new Pair("leads", "tenant_id"), new Pair("users", "t_xxx"), new Pair("comments", "t_xxx"), new Pair("posts", "tenant")]        |   ["leads", "comments"]           |   ["comments", "posts", "users"]      ||  [key("users", "t_xxx", "CONST", "some_schema"), key("posts", "tenant", "CONST", "some_schema")]
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

        @Override
        String toString() {
            return "SetDefaultStatementProducerParametersKey{" +
                    "table='" + table + '\'' +
                    ", column='" + column + '\'' +
                    ", defaultValueDefinition='" + defaultValueDefinition + '\'' +
                    ", schema='" + schema + '\'' +
                    '}';
        }
    }
}
