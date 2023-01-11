package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition
import spock.lang.Unroll

import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.AT_BEGINNING
import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.AT_END


class CustomDefinitionEntryEnricherTest extends AbstractBaseTest {

    def tested = new CustomDefinitionEntryEnricher()

    @Unroll
    def "should set correct custom definition for basic position '#expectedPosition', creation script #cs, drop script #ds and validation scripts #vs"(){
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()

        when:
            tested.enrich(builder, definition)

        then:
            1 * builder.addCustomSQLDefinition(expectedPosition, cs, ds, vs)

        where:
            definition                                                                                        || expectedPosition                              | cs   |   ds |   vs
            new CustomDefinitionEntry().setPosition(AT_END).setCreationScript("X1").setDropScript("X2")       || CustomSQLDefinitionPairDefaultPosition.AT_END | "X1" | "X2" | null
            new CustomDefinitionEntry().setPosition(AT_BEGINNING).setCreationScript("G").setDropScript("H") || CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING  | "G" | "H" | null
            new CustomDefinitionEntry().setPosition(AT_END).setCreationScript("X1").setValidationScripts(["14", "select 1"]) || CustomSQLDefinitionPairDefaultPosition.AT_END    | "X1" | null | ["14", "select 1"]
    }
}
