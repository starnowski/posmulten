package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.context.enrichers.GetCurrentTenantIdFunctionDefinitionEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.SetCurrentTenantIdFunctionDefinitionEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSPolicyEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSSettingsSQLDefinitionsEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantColumnSQLDefinitionsEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantHasAuthoritiesFunctionDefinitionEnricher
import spock.lang.Specification

import static java.util.stream.Collectors.toList

class DefaultSharedSchemaContextBuilderTest extends Specification {

    def "should build shared schema context via enrichers with correct order"()
    {
        given:
            AbstractSharedSchemaContext firstSharedSchemaContext = Mock(AbstractSharedSchemaContext)
            AbstractSharedSchemaContext secondSharedSchemaContext = Mock(AbstractSharedSchemaContext)
            AbstractSharedSchemaContext thirdSharedSchemaContext = Mock(AbstractSharedSchemaContext)
            AbstractSharedSchemaContextEnricher firstSharedSchemaContextEnricher = Mock(AbstractSharedSchemaContextEnricher)
            AbstractSharedSchemaContextEnricher secondSharedSchemaContextEnricher = Mock(AbstractSharedSchemaContextEnricher)
            AbstractSharedSchemaContextEnricher thirdSharedSchemaContextEnricher = Mock(AbstractSharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.setEnrichers([firstSharedSchemaContextEnricher, secondSharedSchemaContextEnricher, thirdSharedSchemaContextEnricher])

        when:
            def result = builder.build()

        then:
            1 * firstSharedSchemaContextEnricher.enrich(_, _) >> firstSharedSchemaContext

        then:
            1 * secondSharedSchemaContextEnricher.enrich(_, _) >> secondSharedSchemaContext

        then:
            1 * thirdSharedSchemaContextEnricher.enrich(_, _) >> thirdSharedSchemaContext

        and: "result should match to the result of the last enricher"
            result.is(thirdSharedSchemaContext)
    }

    def "should have configured the list of enrichers with correct order"()
    {
        given:
            def expectedEnrichersTypeInOrder = [GetCurrentTenantIdFunctionDefinitionEnricher.class, SetCurrentTenantIdFunctionDefinitionEnricher.class, TenantHasAuthoritiesFunctionDefinitionEnricher.class, TenantColumnSQLDefinitionsEnricher.class, TableRLSSettingsSQLDefinitionsEnricher.class, TableRLSPolicyEnricher.class, IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher.class, IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher.class]
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()

        when:
            def enrichers = builder.getEnrichers()

        then:
            enrichers.stream().map({enricher -> enricher.getClass()}).collect(toList()) == expectedEnrichersTypeInOrder
    }
}
