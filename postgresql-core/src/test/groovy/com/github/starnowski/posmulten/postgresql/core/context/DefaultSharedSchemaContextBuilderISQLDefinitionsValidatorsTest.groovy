package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.enrichers.ISharedSchemaContextEnricher
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException
import com.github.starnowski.posmulten.postgresql.core.context.validators.FunctionDefinitionValidator
import com.github.starnowski.posmulten.postgresql.core.context.validators.ISQLDefinitionsValidator
import spock.lang.Specification

class DefaultSharedSchemaContextBuilderISQLDefinitionsValidatorsTest extends Specification {

    def "should pass generated sql definitions to each validator"()
    {
        given:
            ISQLDefinitionsValidator firstValidator = Mock(ISQLDefinitionsValidator)
            ISQLDefinitionsValidator secondValidator = Mock(ISQLDefinitionsValidator)
            ISharedSchemaContextEnricher sharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
                .setSqlDefinitionsValidators([firstValidator, secondValidator])
                .setEnrichers([sharedSchemaContextEnricher])
            List<SQLDefinition> firstEnricherCapturedRequest = null
            List<SQLDefinition> secondEnricherCapturedRequest = null
            ISharedSchemaContext sharedSchemaContext = Mock(ISharedSchemaContext)
            List<SQLDefinition> sqlDefinitions = [Mock(SQLDefinition), Mock(SQLDefinition), Mock(SQLDefinition)]
            sharedSchemaContext.getSqlDefinitions() >> sqlDefinitions

        when:
            builder.build()

        then:
            1 * sharedSchemaContextEnricher.enrich(_, _) >> sharedSchemaContext
        then:
            1 * firstValidator.validate(_) >>
                    {parameters ->
                        firstEnricherCapturedRequest = parameters[0]
                    }
            firstEnricherCapturedRequest == sqlDefinitions

        then:
            1 * secondValidator.validate(_) >>
                    {parameters ->
                        secondEnricherCapturedRequest = parameters[0]
                    }
            secondEnricherCapturedRequest == sqlDefinitions
    }

    def "should rethrow exception thrown by validator in middle"()
    {
        given:
            ISQLDefinitionsValidator firstValidator = Mock(ISQLDefinitionsValidator)
            ISQLDefinitionsValidator secondValidator = Mock(ISQLDefinitionsValidator)
            ISQLDefinitionsValidator thirdValidator = Mock(ISQLDefinitionsValidator)
            ISharedSchemaContextEnricher sharedSchemaContextEnricher = Mock(ISharedSchemaContextEnricher)
            DefaultSharedSchemaContextBuilder builder = new DefaultSharedSchemaContextBuilder()
                .setSqlDefinitionsValidators([firstValidator, secondValidator,thirdValidator])
                .setEnrichers([sharedSchemaContextEnricher])
            def exception = Mock(SharedSchemaContextBuilderException)
            ISharedSchemaContext sharedSchemaContext = Mock(ISharedSchemaContext)
            List<SQLDefinition> sqlDefinitions = [Mock(SQLDefinition), Mock(SQLDefinition), Mock(SQLDefinition)]
            sharedSchemaContext.getSqlDefinitions() >> sqlDefinitions

        when:
            builder.build()

        then:
            1 * sharedSchemaContextEnricher.enrich(_, _) >> sharedSchemaContext

        then:
            1 * firstValidator.validate(_)

        then:
            1 * secondValidator.validate(_) >> { throw exception }
            def ex = thrown(SharedSchemaContextBuilderException)
            ex.is(exception)

        then:
            0 * thirdValidator.validate(_)
    }

    def "should have default list of validators"()
    {
        given:
            DefaultSharedSchemaContextBuilder tested = new DefaultSharedSchemaContextBuilder()

        when:
            def results = tested.prepareSqlDefinitionsValidators(new SharedSchemaContextRequest())

        then:
            results.size() == 1
            results.get(0) instanceof FunctionDefinitionValidator
    }

    def "should disable validators by setting null list"()
    {
        given:
            DefaultSharedSchemaContextBuilder tested = new DefaultSharedSchemaContextBuilder().setSqlDefinitionsValidators(null)

        when:
            def results = tested.prepareSqlDefinitionsValidators(new SharedSchemaContextRequest())

        then:
            results.isEmpty()
    }

    def "should disable validators by setting empty list"()
    {
        given:
            DefaultSharedSchemaContextBuilder tested = new DefaultSharedSchemaContextBuilder().setSqlDefinitionsValidators(null)

        when:
            def results = tested.prepareSqlDefinitionsValidators(new SharedSchemaContextRequest())

        then:
            results.isEmpty()
    }
}
