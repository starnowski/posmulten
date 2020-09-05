package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.core.context.TableRLSSettingsSQLDefinitionsProducer
import spock.lang.Specification
import spock.lang.Unroll

class TableRLSSettingsSQLDefinitionsEnricherTest extends Specification {

    def tested = new TableRLSSettingsSQLDefinitionsEnricher()

    @Unroll
    def "should create all required SQL definition that enables RLS policy but not force policy for schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("users", [:], "tenant", "user_policy")
            builder.createRLSPolicyForTable("comments", [:], "tenant_id", "comments_policy")
            builder.createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "some_table_policy")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def usersTableSQLDefinition1 = Mock(SQLDefinition)
            def usersTableSQLDefinition2 = Mock(SQLDefinition)
            def commentsTableSQLDefinition1 = Mock(SQLDefinition)
            def someTableSQLDefinition1 = Mock(SQLDefinition)
            def someTableSQLDefinition2 = Mock(SQLDefinition)
            def tableRLSSettingsSQLDefinitionsProducer = Mock(TableRLSSettingsSQLDefinitionsProducer)
            tested.setTableRLSSettingsSQLDefinitionsProducer(tableRLSSettingsSQLDefinitionsProducer)
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def someTableKey = tk("some_table", schema)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * tableRLSSettingsSQLDefinitionsProducer.produce(usersTableKey, false) >> [usersTableSQLDefinition1, usersTableSQLDefinition2]
            1 * tableRLSSettingsSQLDefinitionsProducer.produce(commentsTableKey, false) >> [commentsTableSQLDefinition1]
            1 * tableRLSSettingsSQLDefinitionsProducer.produce(someTableKey, false) >> [someTableSQLDefinition1, someTableSQLDefinition2]

            result.getSqlDefinitions().contains(usersTableSQLDefinition1)
            result.getSqlDefinitions().contains(usersTableSQLDefinition2)
            result.getSqlDefinitions().contains(commentsTableSQLDefinition1)
            result.getSqlDefinitions().contains(someTableSQLDefinition1)
            result.getSqlDefinitions().contains(someTableSQLDefinition2)

        and: "sql definitions are added in order returned by component of type SingleTenantColumnSQLDefinitionsProducer"
            result.getSqlDefinitions().indexOf(usersTableSQLDefinition1) < result.getSqlDefinitions().indexOf(usersTableSQLDefinition2)
            result.getSqlDefinitions().indexOf(someTableSQLDefinition1) < result.getSqlDefinitions().indexOf(someTableSQLDefinition2)

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should create all required SQL definition that enables RLS policy and force policy for schema #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.createRLSPolicyForTable("users", [:], "tenant", "user_policy")
            builder.createRLSPolicyForTable("comments", [:], "tenant_id", "comments_policy")
            builder.createRLSPolicyForTable("some_table", [:], "tenant_xxx_id", "some_table_policy")
            builder.setForceRowLevelSecurityForTableOwner(true)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def usersTableSQLDefinition1 = Mock(SQLDefinition)
            def usersTableSQLDefinition2 = Mock(SQLDefinition)
            def commentsTableSQLDefinition1 = Mock(SQLDefinition)
            def someTableSQLDefinition1 = Mock(SQLDefinition)
            def someTableSQLDefinition2 = Mock(SQLDefinition)
            def tableRLSSettingsSQLDefinitionsProducer = Mock(TableRLSSettingsSQLDefinitionsProducer)
            tested.setTableRLSSettingsSQLDefinitionsProducer(tableRLSSettingsSQLDefinitionsProducer)
            def usersTableKey = tk("users", schema)
            def commentsTableKey = tk("comments", schema)
            def someTableKey = tk("some_table", schema)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * tableRLSSettingsSQLDefinitionsProducer.produce(usersTableKey, true) >> [usersTableSQLDefinition1, usersTableSQLDefinition2]
            1 * tableRLSSettingsSQLDefinitionsProducer.produce(commentsTableKey, true) >> [commentsTableSQLDefinition1]
            1 * tableRLSSettingsSQLDefinitionsProducer.produce(someTableKey, true) >> [someTableSQLDefinition1, someTableSQLDefinition2]

            result.getSqlDefinitions().contains(usersTableSQLDefinition1)
            result.getSqlDefinitions().contains(usersTableSQLDefinition2)
            result.getSqlDefinitions().contains(commentsTableSQLDefinition1)
            result.getSqlDefinitions().contains(someTableSQLDefinition1)
            result.getSqlDefinitions().contains(someTableSQLDefinition2)

        and: "sql definitions are added in order returned by component of type SingleTenantColumnSQLDefinitionsProducer"
            result.getSqlDefinitions().indexOf(usersTableSQLDefinition1) < result.getSqlDefinitions().indexOf(usersTableSQLDefinition2)
            result.getSqlDefinitions().indexOf(someTableSQLDefinition1) < result.getSqlDefinitions().indexOf(someTableSQLDefinition2)

        where:
            schema << [null, "public", "some_schema"]
    }

    @Unroll
    def "should not create any sql definitions when there is no request for rls policy in #schema"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequestCopy()
            def context = new SharedSchemaContext()
            def tableRLSSettingsSQLDefinitionsProducer = Mock(TableRLSSettingsSQLDefinitionsProducer)
            tested.setTableRLSSettingsSQLDefinitionsProducer(tableRLSSettingsSQLDefinitionsProducer)

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            0 * tableRLSSettingsSQLDefinitionsProducer.produce(_, _)

            result.getSqlDefinitions().isEmpty()

        where:
            schema << [null, "public", "some_schema"]
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
