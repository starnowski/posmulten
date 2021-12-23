package com.github.starnowski.posmulten.postgresql.core.context


import spock.lang.Specification
import spock.lang.Unroll

class IdentifierLengthValidatorTest extends Specification {

    IdentifierLengthValidator tested

    @Unroll
    def "should confirm that string #value is valid when maximum number is #maxNumber and minimum number is #minNumber"()
    {
        given:
            tested = new IdentifierLengthValidator(minNumber, maxNumber)

        when:
            def result = tested.validate(value)

        then:
            result.valid
            result.message == "Valid"

        where:
            minNumber   |   maxNumber   |   value
            1           |   3           |   "x1"
            1           |   3           |   "1"
            1           |   3           |   "123"
            5           |   73          |   "asdlfkjalskdjl;zxjcvlkxcjvasdkfjasldfajs;ldkjf"
            4           |   210         |   "asdlfkjalskdjl;zxjcvlkxcjvasdkfjasldfajs;ldkjf11111111111111111111111111111111111111111"
    }

    @Unroll
    def "should confirm that string #value is invalid when maximum number is #maxNumber and minimum number is #minNumber"()
    {
        given:
            tested = new IdentifierLengthValidator(minNumber, maxNumber)

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
    def "should confirm that null string is invalid"()
    {
        given:
            tested = new IdentifierLengthValidator()

        when:
            def result = tested.validate(null)

        then:
            !result.valid
            result.message == "Identifier cannot be null"
    }
}
