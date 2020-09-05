package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.context.validators.ISharedSchemaContextRequestValidator
import spock.lang.Specification

class DefaultSharedSchemaContextBuilderValidatorsTest extends Specification {

    def "should validate request in correct order before building shared schema context via enrichers"()
    {
        given:
            ISharedSchemaContextRequestValidator firstValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContextRequestValidator secondValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContext firstSharedSchemaContext = Mock(ISharedSchemaContext)
            ISharedSchemaContextEnricher sharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.setEnrichers([sharedSchemaContextEnricher])

        when:
            def result = builder.build()

        then:
            1 * firstValidator.validate(_)

        then:
            1 * secondValidator.validate(_)

        then:
            1 * sharedSchemaContextEnricher.enrich(_, _) >> firstSharedSchemaContext

        and: "result should match to the result of the last enricher"
            result.is(firstSharedSchemaContext)
    }

    def "should pass the copy or request object to each validator"()
    {
        given:
            ISharedSchemaContextRequestValidator firstValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContextRequestValidator secondValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContextEnricher sharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
            builder.setEnrichers([sharedSchemaContextEnricher])
            def firstEnricherCapturedRequest = null
            def secondEnricherCapturedRequest = null

        when:
            builder.build()

        then:
            1 * firstValidator.validate(_) >>
                    {parameters ->
                        firstEnricherCapturedRequest = parameters[0]
                    }

        then:
            1 * secondValidator.validate(_) >>
                    {parameters ->
                        secondEnricherCapturedRequest = parameters[0]
                    }
            !firstEnricherCapturedRequest.is(secondEnricherCapturedRequest)
    }
}
