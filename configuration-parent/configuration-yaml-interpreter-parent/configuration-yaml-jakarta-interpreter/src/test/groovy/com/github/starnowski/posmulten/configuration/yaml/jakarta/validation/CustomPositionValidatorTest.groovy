package com.github.starnowski.posmulten.configuration.yaml.jakarta.validation

import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.CustomDefinitionEntry
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.*

class CustomPositionValidatorTest extends Specification {

    def "should return true when a position is null because the other validator is responsible for checking this"(){
        given:
            def tested = new CustomPositionValidator()
            def entry = new CustomDefinitionEntry()

        when:
            def result = tested.isValid(entry, null)

        then:
            result
    }

    @Unroll
    def "should return true when position is different than CUSTOM, #position"(){
        given:
            def tested = new CustomPositionValidator()
            def entry = new CustomDefinitionEntry().setPosition(position)

        when:
            def result = tested.isValid(entry, null)

        then:
            result

        where:
            position << [AT_BEGINNING, AT_END]
    }

    @Unroll
    def "should return true when position is CUSTOM and custom position is not empty '#position'"(){
        given:
            def tested = new CustomPositionValidator()
            def entry = new CustomDefinitionEntry().setPosition(CUSTOM).setCustomPosition(position)

        when:
            def result = tested.isValid(entry, null)

        then:
            result

        where:
            position << ["some value", "0", "137"]
    }

    @Unroll
    def "should return false when position is CUSTOM and custom position is invalid : '#position'"(){
        given:
            def tested = new CustomPositionValidator()
            def entry = new CustomDefinitionEntry().setPosition(CUSTOM).setCustomPosition(position)

        when:
            def result = tested.isValid(entry, null)

        then:
            !result

        where:
            position << [null, "", "         "]
    }
}
