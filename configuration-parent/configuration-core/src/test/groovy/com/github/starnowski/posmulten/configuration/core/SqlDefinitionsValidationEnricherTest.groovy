package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import spock.lang.Unroll

class SqlDefinitionsValidationEnricherTest extends AbstractBaseTest {

    def tested = new SqlDefinitionsValidationEnricher()

    @Unroll
    def "should set sql definition validation, expected identifierMaxLength #identifierMaxLength, identifierMinLength #identifierMinLength, validation disabled #disabled " ()
    {
        given:
            def builder = new DefaultSharedSchemaContextBuilder()

        when:
            tested.enrich(builder, sqlDefinitionsValidation)

        then:
            builder.getSharedSchemaContextRequestCopy().getIdentifierMinLength() == identifierMinLength
            builder.getSharedSchemaContextRequestCopy().getIdentifierMaxLength() == identifierMaxLength
            builder.isDisableDefaultSqlDefinitionsValidators() == disabled

        where:
            sqlDefinitionsValidation                                                                                    ||   identifierMaxLength |   identifierMinLength    |   disabled
            new SqlDefinitionsValidation()                                                                              ||  null |   null  |   false
            new SqlDefinitionsValidation().setIdentifierMinLength(15)                                                   ||  null |   15  |   false
            new SqlDefinitionsValidation().setIdentifierMaxLength(23).setDisabled(true)                                 ||  23 |   null  |   true
            new SqlDefinitionsValidation().setIdentifierMinLength(12).setIdentifierMaxLength(332).setDisabled(false)    ||  332 |   12  |   false
            new SqlDefinitionsValidation().setIdentifierMinLength(1).setIdentifierMaxLength(54).setDisabled(true)       ||  54 |   1  |   true
            new SqlDefinitionsValidation().setIdentifierMinLength(12).setIdentifierMaxLength(332).setDisabled(true)     ||  332 |   12  |   true
    }

    def "should not enrich builder when configuration object is null"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()

        when:
            def result = tested.enrich(builder, null)

        then:
            result == builder
            0 * builder.setIdentifierMaxLength(_)
            0 * builder.setIdentifierMinLength(_)
            0 * builder.setDisableDefaultSqlDefinitionsValidators(_)
    }
}
