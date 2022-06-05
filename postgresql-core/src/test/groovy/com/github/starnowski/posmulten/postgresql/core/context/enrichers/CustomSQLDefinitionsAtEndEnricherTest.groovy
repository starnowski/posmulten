package com.github.starnowski.posmulten.postgresql.core.context.enrichers

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.util.Pair

import static com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING
import static com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition.AT_END 

class CustomSQLDefinitionsAtEndEnricherTest extends AbstractCustomSQLDefinitionsEnricherTest {
    @Override
    AbstractCustomSQLDefinitionsEnricher getTestedObject() {
        new CustomSQLDefinitionsAtEndEnricher()
    }

    @Override
    List<Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>> getTestData() {
        [new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(AT_END, definition("validDef")),
         new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(AT_BEGINNING, definition("sql_def_008")),
         new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(AT_END, definition("some_def_x")),
         new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(pp("Custom position"), definition("some_text_008")),
         new Pair<ConstantCustomSQLDefinitionPairPositionProvider, SQLDefinition>(AT_END, definition("sql_def_007"))]
    }

    @Override
    List<String> getExpectedCreationScripts() {
        ["validDef", "some_def_x", "sql_def_007"]
    }

    @Override
    List<String> getIgnoredCreationScripts() {
        ["sql_def_008", "some_text_008"]
    }
}
