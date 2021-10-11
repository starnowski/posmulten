package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException
import spock.lang.Specification
import spock.lang.Unroll

class IdentifierLengthValidatorTest extends Specification {

    def tested = new IdentifierLengthValidator()

    @Unroll
    def "should by default init validator with default maximum number of characters allowed in postgres"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()

        when:
            tested.init(request)

        then:
            tested.getIdentifierMaxLength() == 63
            tested.getIdentifierMinLength() == 1
    }

    @Unroll
    def "should init validator with default maximum number #maxNumber and minimum number #minNumber"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()
            request.setIdentifierMinLength(minNumber)
            request.setIdentifierMaxLength(maxNumber)

        when:
            tested.init(request)

        then:
            tested.getIdentifierMaxLength() == maxNumber
            tested.getIdentifierMinLength() == minNumber

        where:
            minNumber   |   maxNumber
            1           |   3
            5           |   73
            4           |   210
    }

    @Unroll
    def "should confirm that string #value is valid when maximum number is #maxNumber and minimum number is #minNumber"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()
            request.setIdentifierMinLength(minNumber)
            request.setIdentifierMaxLength(maxNumber)
            tested.init(request)

        when:
            def result = tested.validate(value)

        then:
            result.valid
            result.message == "Valid"

        where:
            minNumber   |   maxNumber   |   value
            1           |   3           |   "x1"
            5           |   73          |   "asdlfkjalskdjl;zxjcvlkxcjvasdkfjasldfajs;ldkjf"
            4           |   210         |   "asdlfkjalskdjl;zxjcvlkxcjvasdkfjasldfajs;ldkjf11111111111111111111111111111111111111111"
    }

    @Unroll
    def "should confirm that string #value is invalid when maximum number is #maxNumber and minimum number is #minNumber"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()
            request.setIdentifierMinLength(minNumber)
            request.setIdentifierMaxLength(maxNumber)
            tested.init(request)

        when:
            def result = tested.validate(value)

        then:
            !result.valid
            result.message == message

        where:
            minNumber   |   maxNumber   |   value                                   ||  message
            1           |   3           |   "x1xxxc"                                ||  "Identifier 'x1xxxc' is invalid, the length must be between 1 and 3"
            5           |   20          |   "Tooo_large_string_______ddddssxxz"     ||  "Identifier 'Tooo_large_string_______ddddssxxz' is invalid, the length must be between 5 and 20"
            4           |   30          |   "asdlfkjalskdjl;zxjcvlkxcjvasdkfjas"    ||  "Identifier 'asdlfkjalskdjl;zxjcvlkxcjvasdkfjas' is invalid, the length must be between 4 and 30"
    }

    @Unroll
    def "should throw exception of type 'SharedSchemaContextBuilderException' when identifierMinLength is less or equal to zero (#identifierMinLength)"()
    {
        given:
            SharedSchemaContextRequest request = new SharedSchemaContextRequest()
            request.setIdentifierMinLength(identifierMinLength)

        when:
            tested.init(request)

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
            tested.init(request)

        then:
            def ex = thrown(InvalidSharedSchemaContextRequestException.class)

        and: "exception should have correct message"
            ex.message == "The identifierMaxLength property value can not be less or equal to zero"

        where:
            identifierMaxLength << [0, -1, -100]
    }
}
