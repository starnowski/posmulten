package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.*
import com.github.starnowski.posmulten.postgresql.core.rls.TenantHasAuthoritiesFunctionInvocationFactory
import spock.lang.Specification
import spock.lang.Unroll

class TableRLSPolicyEnricherTest extends Specification {

    def tested = new TableRLSPolicyEnricher()

    @Unroll
    def "should create all required SQL definition that creates RLS policy for schema #schema and default tenant id column #defaultTenantIdColumn and grantee #grantee"()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder(schema)
            builder.setDefaultTenantIdColumn(defaultTenantIdColumn)
            builder.createRLSPolicyForColumn("posts", [:], "tenant", "posts_policy")
            builder.createRLSPolicyForColumn("comments", [:], "tenant_id", "comments_policy")
            def sharedSchemaContextRequest = builder.getSharedSchemaContextRequest()
            def context = new SharedSchemaContext()
            def postsTableSQLDefinition1 = Mock(SQLDefinition)
            def postsTableSQLDefinition2 = Mock(SQLDefinition)
            def commentsTableSQLDefinition1 = Mock(SQLDefinition)
            def tableRLSPolicySQLDefinitionsProducer = Mock(TableRLSPolicySQLDefinitionsProducer)
            def tenantHasAuthoritiesFunctionInvocationFactory = Mock(TenantHasAuthoritiesFunctionInvocationFactory)
            tested.setTableRLSPolicySQLDefinitionsProducer(tableRLSPolicySQLDefinitionsProducer)
            def postsTableKey = tk("posts", schema)
            def commentsTableKey = tk("comments", schema)
            def parametersBuilder = new TableRLSPolicySQLDefinitionsProducerParameters.TableRLSPolicySQLDefinitionsProducerParametersBuilder()
            def postsTableParameters = parametersBuilder.withDefaultTenantIdColumn(defaultTenantIdColumn)
                    .withTenantIdColumn("tenant")
                    .withTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                    .withGrantee(grantee)
                    .withTableKey(postsTableKey)
                    .withPolicyName("posts_policy").build()

            def commentsTableParameters = parametersBuilder.withDefaultTenantIdColumn(defaultTenantIdColumn)
                    .withTenantIdColumn("tenant_id")
                    .withTenantHasAuthoritiesFunctionInvocationFactory(tenantHasAuthoritiesFunctionInvocationFactory)
                    .withGrantee(grantee)
                    .withTableKey(commentsTableKey)
                    .withPolicyName("comments_policy").build()

        when:
            def result = tested.enrich(context, sharedSchemaContextRequest)

        then:
            1 * tableRLSPolicySQLDefinitionsProducer.produce(postsTableParameters) >> [postsTableSQLDefinition1, postsTableSQLDefinition2]
            1 * tableRLSPolicySQLDefinitionsProducer.produce(commentsTableParameters) >> [commentsTableSQLDefinition1]

            result.getSqlDefinitions().contains(postsTableSQLDefinition1)
            result.getSqlDefinitions().contains(postsTableSQLDefinition2)
            result.getSqlDefinitions().contains(commentsTableSQLDefinition1)

        and: "sql definitions are added in order returned by component of type TableRLSPolicySQLDefinitionsProducer"
            result.getSqlDefinitions().indexOf(postsTableSQLDefinition1) < result.getSqlDefinitions().indexOf(postsTableSQLDefinition2)

        where:
            schema          |   defaultTenantIdColumn   |   grantee
            null            |   "tenant_id"             |   "core-user"
            "public"        | "tenant"                  |   "owner"
            "some_schema"   | "t_column"                |   "admin"
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
