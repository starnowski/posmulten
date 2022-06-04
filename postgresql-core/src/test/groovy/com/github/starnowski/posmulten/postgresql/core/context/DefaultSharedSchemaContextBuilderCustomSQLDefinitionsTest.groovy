package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import spock.lang.Specification

import static com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition.AT_BEGINNING
import static com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition.AT_END
import static java.util.stream.Collectors.toList

class DefaultSharedSchemaContextBuilderCustomSQLDefinitionsTest extends Specification {

    private static final String EXPECTED_DEFAULT_SQL_STATEMENT = "SELECT 1"

    def "should add custom SQL definitions with their positions "() {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
            def sqlDefinitions = [Mock(SQLDefinition), Mock(SQLDefinition), Mock(SQLDefinition), Mock(SQLDefinition), Mock(SQLDefinition)]
            List<CustomSQLDefinitionPairPositionProvider> positionProviders = [AT_END, AT_BEGINNING, new TestCustomSQLDefinitionPairPositionProvider("xxx"), new TestCustomSQLDefinitionPairPositionProvider("test1"), AT_BEGINNING ]
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

    def "should add custom SQL definitions defined only by their creation script and their positions"() {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
            def creationScripts = ["SELECT 89", "SELECT 15", "insert into some_table blablal;", "SELECT 500", "Alter TabLe"]
            List<CustomSQLDefinitionPairPositionProvider> positionProviders = [AT_END, AT_BEGINNING, new TestCustomSQLDefinitionPairPositionProvider("xxx"), new TestCustomSQLDefinitionPairPositionProvider("test1"), AT_BEGINNING ]
            def expectedPositions = positionProviders.stream().map({ it -> it.getPosition() }).collect(toList())
            def lastBulderReference = tested

            when:
            for (int i = 0; i < creationScripts.size(); i++) {
                lastBulderReference = lastBulderReference.addCustomSQLDefinition(positionProviders[i], creationScripts[i])
            }

        then:
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.position == expectedPositions
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.createScript == creationScripts
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.dropScript == [EXPECTED_DEFAULT_SQL_STATEMENT, EXPECTED_DEFAULT_SQL_STATEMENT, EXPECTED_DEFAULT_SQL_STATEMENT, EXPECTED_DEFAULT_SQL_STATEMENT, EXPECTED_DEFAULT_SQL_STATEMENT]
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.checkingStatements == [[EXPECTED_DEFAULT_SQL_STATEMENT], [EXPECTED_DEFAULT_SQL_STATEMENT], [EXPECTED_DEFAULT_SQL_STATEMENT], [EXPECTED_DEFAULT_SQL_STATEMENT], [EXPECTED_DEFAULT_SQL_STATEMENT]]

        and: "reference to builder should be the same"
            lastBulderReference == tested
    }

    def "should add custom SQL definitions defined only by their creation and drop script and their positions"() {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
            def creationScripts = ["SELECT 113", "SELECT 88", "insert into some_table values;", "SELECT 500", "Alter TabLe"]
            def dropScripts = ["DELETE", "SELECT 57", "delete FROM _xtable;", "DROP TABLE", "Alter TabLe"]
            List<CustomSQLDefinitionPairPositionProvider> positionProviders = [AT_END, AT_BEGINNING, new TestCustomSQLDefinitionPairPositionProvider("xxx"), new TestCustomSQLDefinitionPairPositionProvider("test1"), AT_BEGINNING ]
            def expectedPositions = positionProviders.stream().map({ it -> it.getPosition() }).collect(toList())
            def lastBulderReference = tested

        when:
            for (int i = 0; i < creationScripts.size(); i++) {
                lastBulderReference = lastBulderReference.addCustomSQLDefinition(positionProviders[i], creationScripts[i], dropScripts[i])
            }

        then:
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.position == expectedPositions
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.createScript == creationScripts
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.dropScript == dropScripts
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.checkingStatements == [[EXPECTED_DEFAULT_SQL_STATEMENT], [EXPECTED_DEFAULT_SQL_STATEMENT], [EXPECTED_DEFAULT_SQL_STATEMENT], [EXPECTED_DEFAULT_SQL_STATEMENT], [EXPECTED_DEFAULT_SQL_STATEMENT]]

        and: "reference to builder should be the same"
            lastBulderReference == tested
    }

    def "should add custom SQL definitions defined only by their creation and drop script, checking statements and their positions"() {
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
            def creationScripts = ["SELECT 113", "SELECT 88", "insert into some_table values;", "SELECT 500", "Alter TabLe"]
            def dropScripts = ["DELETE", "SELECT 57", "delete FROM _xtable;", "DROP TABLE", "Alter TabLe"]
            def checkingStatements = [["SELECT 144"], ["SELECT 1 from tab"], ["SELECT 43", "SELECT 88", "SELECT 91"], ["SELECT 776"], ["SELECT 13", "SELECT 1 FROM table"]]
            List<CustomSQLDefinitionPairPositionProvider> positionProviders = [AT_END, AT_BEGINNING, new TestCustomSQLDefinitionPairPositionProvider("xxx"), new TestCustomSQLDefinitionPairPositionProvider("test1"), AT_BEGINNING ]
            def expectedPositions = positionProviders.stream().map({ it -> it.getPosition() }).collect(toList())
            def lastBulderReference = tested

        when:
            for (int i = 0; i < creationScripts.size(); i++) {
                lastBulderReference = lastBulderReference.addCustomSQLDefinition(positionProviders[i], creationScripts[i], dropScripts[i], checkingStatements[i])
            }

        then:
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.position == expectedPositions
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.createScript == creationScripts
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.dropScript == dropScripts
            tested.sharedSchemaContextRequestCopy.customSQLDefinitionPairs.sqlDefinition.checkingStatements == checkingStatements

        and: "reference to builder should be the same"
            lastBulderReference == tested
    }

    def "should add custom sql definition in correct position"(){
        given:
            def tested = new DefaultSharedSchemaContextBuilder()
            tested.setGrantee("john_doe")
            def creation1 = "creat1"
            def creation2 = "creat2"
            def creation3 = "creat3"
            def creation4 = "creat4"
            def drop1 = "creat1"
            def drop2 = "creat2"
            def drop3 = "creat3"
            def drop4 = "creat4"
            def checkingStatements1 = ["SELECT 13", "SELECT 1 FROM table"]
            def checkingStatements2 = ["SELECT 54 FROM table"]
            def checkingStatements3 = ["SELECT 88"]
            def checkingStatements4 = ["SELECT 46", "SELECT 1 FROM non_table"]

            tested.addCustomSQLDefinition(AT_END, creation1, drop1, checkingStatements1)
            tested.addCustomSQLDefinition(AT_BEGINNING, creation2, drop2, checkingStatements2)
            tested.addCustomSQLDefinition(AT_BEGINNING, creation3, drop3, checkingStatements3)
            tested.addCustomSQLDefinition(AT_END, creation4, drop4, checkingStatements4)

        when:
            def context = tested.build()

        then:
            context.getSqlDefinitions()[0].createScript == creation2
            context.getSqlDefinitions()[0].dropScript == drop2
            context.getSqlDefinitions()[0].checkingStatements == checkingStatements2
            context.getSqlDefinitions()[2].createScript == creation3
            context.getSqlDefinitions()[2].dropScript == drop3
            context.getSqlDefinitions()[2].checkingStatements == checkingStatements3

            def lastIndex = context.getSqlDefinitions().size() - 1
            context.getSqlDefinitions()[lastIndex - 1].createScript == creation1
            context.getSqlDefinitions()[lastIndex - 1].dropScript == drop1
            context.getSqlDefinitions()[lastIndex - 1].checkingStatements == checkingStatements1
            context.getSqlDefinitions()[lastIndex].createScript == creation4
            context.getSqlDefinitions()[lastIndex].dropScript == drop4
            context.getSqlDefinitions()[lastIndex].checkingStatements == checkingStatements4
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
