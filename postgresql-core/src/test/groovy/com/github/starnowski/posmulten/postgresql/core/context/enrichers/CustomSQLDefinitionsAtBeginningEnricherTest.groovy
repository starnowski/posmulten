package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.util.Pair

import static com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING
import static com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition.AT_END

class CustomSQLDefinitionsAtBeginningEnricherTest extends AbstractCustomSQLDefinitionsEnricherTest {
    @Override
    AbstractCustomSQLDefinitionsEnricher getTestedObject() {
        new CustomSQLDefinitionsAtBeginningEnricher()
    }

    @Override
    List<Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>> getTestData() {
        [new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(AT_END, definition("ddyzds")),
         new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(AT_BEGINNING, definition("test443")),
         new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(AT_BEGINNING, definition("texx13")),
         new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(pp("Custom position"), definition("ignoredX13")),
         new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(AT_BEGINNING, definition("final"))]
    }

    @Override
    List<String> getExpectedCreationScripts() {
        ["test443", "texx13", "final"]
    }

    @Override
    List<String> getIgnoredCreationScripts() {
        ["ddyzds", "ignoredX13"]
    }
}
