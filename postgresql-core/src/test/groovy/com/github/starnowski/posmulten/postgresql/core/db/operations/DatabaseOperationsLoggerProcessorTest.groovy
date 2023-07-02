package com.github.starnowski.posmulten.postgresql.core.db.operations

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import nl.altindag.log.LogCaptor
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource
import java.sql.Connection

class DatabaseOperationsLoggerProcessorTest extends Specification {

    @Unroll
    def "should log all scripts #expectedCreateScriptsMessages, #expectedDropScriptsMessages, #expectedCheckScriptsMessages"(){
        given:
            LogCaptor logCaptor = LogCaptor.forClass(DatabaseOperationsLoggerProcessor)
            def tested = new DatabaseOperationsLoggerProcessor()
            def expectedInfoLogs = new ArrayList()
            expectedInfoLogs.add("Creation scripts")
            expectedInfoLogs.addAll(expectedCreateScriptsMessages)
            expectedInfoLogs.add("Drop scripts")
            expectedInfoLogs.addAll(expectedDropScriptsMessages)
            expectedInfoLogs.add("Validation scripts")
            expectedInfoLogs.addAll(expectedCheckScriptsMessages)

        when:
            tested.run((DataSource)null, definitions)

        then:
            logCaptor.getInfoLogs() == expectedInfoLogs

        where:
            definitions    ||  expectedCreateScriptsMessages   |  expectedDropScriptsMessages |  expectedCheckScriptsMessages
            [sqlDef("cre1", "DROPX", ["check1", "analyst"]), sqlDef("cre2", "drop All", ["check", "check15"])]  ||  ["cre1", "cre2"]    |   ["DROPX", "drop All"]   |   ["check1", "analyst", "check", "check15"]
    }

    @Unroll
    def "should log all scripts for connection #expectedCreateScriptsMessages, #expectedDropScriptsMessages, #expectedCheckScriptsMessages"(){
        given:
            LogCaptor logCaptor = LogCaptor.forClass(DatabaseOperationsLoggerProcessor)
            def tested = new DatabaseOperationsLoggerProcessor()
            def expectedInfoLogs = new ArrayList()
            expectedInfoLogs.add("Creation scripts")
            expectedInfoLogs.addAll(expectedCreateScriptsMessages)
            expectedInfoLogs.add("Drop scripts")
            expectedInfoLogs.addAll(expectedDropScriptsMessages)
            expectedInfoLogs.add("Validation scripts")
            expectedInfoLogs.addAll(expectedCheckScriptsMessages)

        when:
            tested.run((Connection)null, definitions)

        then:
            logCaptor.getInfoLogs() == expectedInfoLogs

        where:
            definitions    ||  expectedCreateScriptsMessages   |  expectedDropScriptsMessages |  expectedCheckScriptsMessages
            [sqlDef("cre1", "DROPX", ["check1", "analyst"]), sqlDef("cre2", "drop All", ["check", "check15"])]  ||  ["cre1", "cre2"]    |   ["DROPX", "drop All"]   |   ["check1", "analyst", "check", "check15"]
    }

    private static SQLDefinition sqlDef(String createScript, String dropScript, List<String> checkScripts){
        SQLDefinition definition = Mockito.mock(SQLDefinition)
        Mockito.when(definition.getCreateScript()).thenReturn(createScript)
        Mockito.when(definition.getDropScript()).thenReturn(dropScript)
        Mockito.when(definition.getCheckingStatements()).thenReturn(checkScripts)
        definition
    }
}
