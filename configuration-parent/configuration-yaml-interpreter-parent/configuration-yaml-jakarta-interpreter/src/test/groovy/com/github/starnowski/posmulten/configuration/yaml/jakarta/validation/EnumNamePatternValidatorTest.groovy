package com.github.starnowski.posmulten.configuration.yaml.jakarta.validation

import spock.lang.Specification

class EnumNamePatternValidatorTest extends Specification {

    def "should return true when value is null"(){
        given:
            def tested = new EnumNamePatternValidator()

        when:
            def result = tested.isValid(null, null)

        then:
            result
    }
}
