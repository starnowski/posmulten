package com.github.starnowski.posmulten.postgresql.core.context.validators.factories

import com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException
import spock.lang.Specification
import spock.lang.Unroll

class IdentifierLengthValidatorFactoryTest extends Specification {

    def tested = new IdentifierLengthValidatorFactory()

    @Unroll
    def "should by default build validator with default maximum number of characters allowed in postgres"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()

        when:
            def result = tested.build(request)

        then:
            result.getIdentifierMaxLength() == 63
            result.getIdentifierMinLength() == 1
    }

    @Unroll
    def "should build validator with default maximum number #maxNumber and minimum number #minNumber"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()
            request.setIdentifierMinLength(minNumber)
            request.setIdentifierMaxLength(maxNumber)

        when:
            def result = tested.build(request)

        then:
            result.getIdentifierMaxLength() == maxNumber
            result.getIdentifierMinLength() == minNumber

        where:
            minNumber   |   maxNumber
            1           |   3
            5           |   73
            4           |   210
    }

    @Unroll
    def "should throw exception of type 'SharedSchemaContextBuilderException' when identifierMinLength is less or equal to zero (#identifierMinLength)"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()
            request.setIdentifierMinLength(identifierMinLength)

        when:
            tested.build(request)

        then:
            def ex = thrown(InvalidSharedSchemaContextRequestException.class)

        and: "exception should have correct message"
            ex.message == "The identifierMinLength property value can not be less or equal to zero"

        where:
            identifierMinLength << [0, -1, -100]
    }

    @Unroll
    def "should throw exception of type 'SharedSchemaContextBuilderException' when identifierMaxLength is less or equal to zero (#identifierMaxLength)"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()
            request.setIdentifierMaxLength(identifierMaxLength)

        when:
            tested.build(request)

        then:
            def ex = thrown(InvalidSharedSchemaContextRequestException.class)

        and: "exception should have correct message"
            ex.message == "The identifierMaxLength property value can not be less or equal to zero"

        where:
            identifierMaxLength << [0, -1, -100]
    }
}
