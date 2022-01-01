package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.context.enrichers.CurrentTenantIdPropertyTypeEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.DefaultValueForTenantColumnEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.GetCurrentTenantIdFunctionDefinitionEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantValidFunctionInvocationFactoryEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.ISharedSchemaContextEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantIdentifierValidConstraintEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.SetCurrentTenantIdFunctionDefinitionEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSPolicyEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSSettingsSQLDefinitionsEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantColumnSQLDefinitionsEnricher
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantHasAuthoritiesFunctionDefinitionEnricher
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException
import spock.lang.Specification

import static java.util.stream.Collectors.toList

class DefaultSharedSchemaContextBuilderEnrichersTest extends Specification {

    def "should build shared schema context via enrichers with correct order"()
    {
        given:
            ISharedSchemaContext firstSharedSchemaContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext secondSharedSchemaContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext thirdSharedSchemaContext = Mock(ISharedSchemaContext)
            ISharedSchemaContextEnricher firstSharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            ISharedSchemaContextEnricher secondSharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            ISharedSchemaContextEnricher thirdSharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = builderWithoutValidators()
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
            def expectedEnrichersTypeInOrder = [GetCurrentTenantIdFunctionDefinitionEnricher.class,
                                                SetCurrentTenantIdFunctionDefinitionEnricher.class, TenantHasAuthoritiesFunctionDefinitionEnricher.class,
                                                IsTenantValidFunctionInvocationFactoryEnricher.class, TenantColumnSQLDefinitionsEnricher.class,
                                                TableRLSSettingsSQLDefinitionsEnricher.class, TableRLSPolicyEnricher.class,
                                                IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher.class, IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher.class,
                                                IsTenantIdentifierValidConstraintEnricher.class, DefaultValueForTenantColumnEnricher.class,
                                                CurrentTenantIdPropertyTypeEnricher.class]
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()

        when:
            def enrichers = builder.getEnrichersCopy()

        then:
            enrichers.stream().map({enricher -> enricher.getClass()}).collect(toList()) == expectedEnrichersTypeInOrder
    }

    def "should pass the copy or request object to each enricher"()
    {
        given:
            ISharedSchemaContext firstSharedSchemaContext = Mock(ISharedSchemaContext)
            ISharedSchemaContext secondSharedSchemaContext = Mock(ISharedSchemaContext)
            ISharedSchemaContextEnricher firstSharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            ISharedSchemaContextEnricher secondSharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = builderWithoutValidators()
            builder.setEnrichers([firstSharedSchemaContextEnricher, secondSharedSchemaContextEnricher])
            def firstEnricherCapturedRequest = null
            def secondEnricherCapturedRequest = null
            def firstEnricherCapturedContext = null
            def secondEnricherCapturedContext = null

        when:
            def result = builder.build()

        then:
            1 * firstSharedSchemaContextEnricher.enrich(_, _) >>
                    {parameters ->
                        firstEnricherCapturedContext = parameters[0]
                        firstEnricherCapturedRequest = parameters[1]
                        firstSharedSchemaContext
                    }

        then:
            1 * secondSharedSchemaContextEnricher.enrich(_, _) >>
                    {parameters ->
                        secondEnricherCapturedContext = parameters[0]
                        secondEnricherCapturedRequest = parameters[1]
                        secondSharedSchemaContext
                    }
            !firstEnricherCapturedRequest.is(secondEnricherCapturedRequest)

        and: "the context passed to second enricher should the same returned by first enricher"
            secondEnricherCapturedContext.is(firstSharedSchemaContext)

        and: "result should match to the result of the last enricher"
            result.is(secondSharedSchemaContext)
    }

    def "should rethrow exception thrown by enricher in middle"()
    {
        given:
            ISharedSchemaContext firstSharedSchemaContext = Mock(ISharedSchemaContext)
            ISharedSchemaContextEnricher firstSharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            ISharedSchemaContextEnricher secondSharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            ISharedSchemaContextEnricher thirdSharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = builderWithoutValidators()
            builder.setEnrichers([firstSharedSchemaContextEnricher, secondSharedSchemaContextEnricher, thirdSharedSchemaContextEnricher])
            def exception = Mock(SharedSchemaContextBuilderException)

        when:
            builder.build()

        then:
            1 * firstSharedSchemaContextEnricher.enrich(_, _) >> firstSharedSchemaContext

        then:
            1 * secondSharedSchemaContextEnricher.enrich(_, _) >> { throw exception }
            def ex = thrown(SharedSchemaContextBuilderException)
            ex.is(exception)

        then:
            0 * thirdSharedSchemaContextEnricher.enrich(_, _)
    }

    def builderWithoutValidators()
    {
        def builder = new DefaultSharedSchemaContextBuilder()
        builder.setValidators([])
    }
}
