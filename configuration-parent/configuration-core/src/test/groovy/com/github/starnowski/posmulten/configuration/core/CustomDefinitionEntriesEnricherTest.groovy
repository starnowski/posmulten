package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry
import spock.lang.Unroll

class CustomDefinitionEntriesEnricherTest extends AbstractBaseTest {

    @Unroll
    def "should invoke component that handles custom definitions"()
    {
        given:
            def component = Mock(CustomDefinitionEntryEnricher)
            def tested = new CustomDefinitionEntriesEnricher(component)
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def cd1 = new CustomDefinitionEntry().setPosition("AT_BEGINING")
            def cd2 = new CustomDefinitionEntry().setPosition("AT_END")

        when:
        def result = tested.enrich(builder, [cd1, cd2])

        then:
            result == builder
            1 * component.enrich(builder, cd1)
            1 * component.enrich(builder, cd2)

        and: "do not invoke builder"
            0 * builder._

    }

    @Unroll
    def "should not invoke any builder's component method when invalid object is passed (#message)"()
    {
        given:
            def component = Mock(CustomDefinitionEntryEnricher)
            def tested = new CustomDefinitionEntriesEnricher(component)
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()

        when:
            def result = tested.enrich(builder, entries)

        then:
            result == builder
            0 * component._

        and: "do not invoke builder"
            0 * builder._

        where:
            entries                                                                     |   message
            null                                                                        |   "null object"
            []                                                                          |   "empty list"
    }

    def "should use correct component type"()
    {
        given:
            def tested = new CustomDefinitionEntriesEnricher()

        when:
            def result = tested.getCustomDefinitionEntryEnricher()

        then:
            result
            result.getClass().equals(CustomDefinitionEntryEnricher.class)
    }
}
