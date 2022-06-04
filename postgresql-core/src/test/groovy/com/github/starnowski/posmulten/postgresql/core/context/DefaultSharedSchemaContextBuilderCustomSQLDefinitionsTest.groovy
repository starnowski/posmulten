package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import spock.lang.Specification

import static java.util.stream.Collectors.toList

class DefaultSharedSchemaContextBuilderCustomSQLDefinitionsTest extends Specification {

    def "should add custom SQL definitions with their positions "() {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
            def sqlDefinitions = [Mock(SQLDefinition), Mock(SQLDefinition), Mock(SQLDefinition), Mock(SQLDefinition), Mock(SQLDefinition)]
            List<CustomSQLDefinitionPairPositionProvider> positionProviders = [CustomSQLDefinitionPairDefaultPosition.AT_END, CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING, new TestCustomSQLDefinitionPairPositionProvider("xxx"), new TestCustomSQLDefinitionPairPositionProvider("test1"), CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING ]
            def expectedPositions = positionProviders.stream().map({ it -> it.getPosition() }).collect(toList())
            def lastBulderReference = tested

        when:
            for (int i = 0; i < sqlDefinitions.size(); i++) {
                lastBulderReference = lastBulderReference.addCustomSQLDefinition(positionProviders[i], sqlDefinitions[i])
            }

        then:
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.position == expectedPositions
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition == sqlDefinitions

        and: "reference to builder should be the same"
            lastBulderReference == tested
    }

    private static class TestCustomSQLDefinitionPairPositionProvider implements CustomSQLDefinitionPairPositionProvider {

        private String position

        TestCustomSQLDefinitionPairPositionProvider(String position){
            this.position = position
        }

        @Override
        String getPosition() {
            position
        }
    }
}
