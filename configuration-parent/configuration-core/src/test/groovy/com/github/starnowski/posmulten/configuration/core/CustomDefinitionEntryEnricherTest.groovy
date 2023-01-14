package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition
import com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider
import spock.lang.Unroll

import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.AT_BEGINNING
import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.AT_END
import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.CUSTOM


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

    @Unroll
    def "should set correct custom definition with custom position '#expectedPosition', creation script #cs, drop script #ds and validation scripts #vs"(){
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            CustomSQLDefinitionPairPositionProvider passedCustomPositionProvider = (CustomSQLDefinitionPairPositionProvider)null
            definition.setPosition(CUSTOM)

        when:
            tested.enrich(builder, definition)

        then:
            1 * builder.addCustomSQLDefinition(_, cs, ds, vs) >> {
                parameters ->
                    passedCustomPositionProvider = (CustomSQLDefinitionPairPositionProvider)parameters[0]
                    builder
            }
            passedCustomPositionProvider.getPosition() == expectedPosition

        where:
            definition                                                                                      || expectedPosition | cs   |   ds |   vs
            new CustomDefinitionEntry().setCreationScript("X1").setDropScript("X2").setCustomPosition("Some where near")                         || "Some where near" | "X1" | "X2" | null
            new CustomDefinitionEntry().setCreationScript("G").setDropScript("H").setCustomPosition("-1")                             || "-1"  | "G" | "H" | null
            new CustomDefinitionEntry().setCreationScript("X1").setValidationScripts(["14", "select 1"]).setCustomPosition("37")      || "37"    | "X1" | null | ["14", "select 1"]
    }
}
