package com.github.starnowski.posmulten.postgresql.core.context


import com.github.starnowski.posmulten.postgresql.core.context.enrichers.ISharedSchemaContextEnricher
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException
import com.github.starnowski.posmulten.postgresql.core.context.validators.CreateTenantColumnTableMappingSharedSchemaContextRequestValidator
import com.github.starnowski.posmulten.postgresql.core.context.validators.ForeignKeysMappingSharedSchemaContextRequestValidator
import com.github.starnowski.posmulten.postgresql.core.context.validators.ISharedSchemaContextRequestValidator
import com.github.starnowski.posmulten.postgresql.core.context.validators.TablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedSharedSchemaContextRequestValidator
import spock.lang.Specification

import static java.util.stream.Collectors.toList

class DefaultSharedSchemaContextBuilderValidatorsTest extends Specification {

    def "should validate request in correct order before building shared schema context via enrichers"()
    {
        given:
            ISharedSchemaContextRequestValidator firstValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContextRequestValidator secondValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContext firstSharedSchemaContext = Mock(ISharedSchemaContext)
            ISharedSchemaContextEnricher sharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
                .setValidators([firstValidator, secondValidator])
                .setEnrichers([sharedSchemaContextEnricher])

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
                .setValidators([firstValidator, secondValidator])
                .setEnrichers([sharedSchemaContextEnricher])
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

    def "should rethrow exception thrown by validator in middle"()
    {
        given:
            ISharedSchemaContextRequestValidator firstValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContextRequestValidator secondValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContextRequestValidator thirdValidator = Mock(ISharedSchemaContextRequestValidator)
            ISharedSchemaContextEnricher sharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
                .setValidators([firstValidator, secondValidator,thirdValidator])
                .setEnrichers([sharedSchemaContextEnricher])
            def exception = Mock(SharedSchemaContextBuilderException)

        when:
            builder.build()

        then:
            1 * firstValidator.validate(_)

        then:
            1 * secondValidator.validate(_) >> { throw exception }
            def ex = thrown(SharedSchemaContextBuilderException)
            ex.is(exception)

        then:
            0 * sharedSchemaContextEnricher.enrich(_, _)
    }

    def "should have configured the list of validators with correct order"()
    {
        given:
            def expectedValidatorsTypeInOrder = [ForeignKeysMappingSharedSchemaContextRequestValidator.class, CreateTenantColumnTableMappingSharedSchemaContextRequestValidator.class, TablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedSharedSchemaContextRequestValidator.class]
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()

        when:
            def validators = builder.getValidatorsCopy()

        then:
            validators.stream().map({validator -> validator.getClass()}).collect(toList()) == expectedValidatorsTypeInOrder
    }
}
