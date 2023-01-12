package com.github.starnowski.posmulten.configuration.yaml.validation

import com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.AT_BEGINNING
import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.AT_END
import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.CUSTOM

class CustomPositionValidatorTest extends Specification {

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
    def "should return true when position is different than CUSTOM and custom position is not empty '#position'"(){
        given:
            def tested = new CustomPositionValidator()
            def entry = new CustomDefinitionEntry().setPosition(CUSTOM).setCustomPosition()

        when:
            def result = tested.isValid(entry, null)

        then:
            result

        where:
            position << ["some value", "0", "137"]
    }
}
