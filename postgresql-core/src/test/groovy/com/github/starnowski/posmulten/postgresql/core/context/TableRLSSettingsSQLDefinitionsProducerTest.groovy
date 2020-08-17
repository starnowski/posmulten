package com.github.starnowski.posmulten.postgresql.core.context


import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.rls.EnableRowLevelSecurityProducer
import com.github.starnowski.posmulten.postgresql.core.rls.ForceRowLevelSecurityProducer
import spock.lang.Specification
import spock.lang.Unroll

class TableRLSSettingsSQLDefinitionsProducerTest extends Specification {

    def tested = new TableRLSSettingsSQLDefinitionsProducer()

    @Unroll
    def "should create all required SQL definition that enables RLS policy but not force policy for table owner in table #tableKey"()
    {
        given:
            def enableRowLevelSecurityProducer = Mock(EnableRowLevelSecurityProducer)
            def forceRowLevelSecurityProducer = Mock(ForceRowLevelSecurityProducer)
            def enableRowLevelSecurityProducerSQLDefinition = Mock(SQLDefinition)
            tested.setEnableRowLevelSecurityProducer(enableRowLevelSecurityProducer)
            tested.setForceRowLevelSecurityProducer(forceRowLevelSecurityProducer)

        when:
            def results = tested.produce(tableKey, false)

        then:
            1 * enableRowLevelSecurityProducer.produce(tableKey.getTable(), tableKey.getSchema()) >> enableRowLevelSecurityProducerSQLDefinition
            0 * forceRowLevelSecurityProducer.produce(_, _)
            results == [enableRowLevelSecurityProducerSQLDefinition]

        where:
            tableKey                        |   _
            tk("users", null)               |   _
            tk("users", "public")           |   _
            tk("users", "other_schema")     |   _
            tk("some_tab", "other_schema")  |   _
            tk("some_tab", "other_schema")  |   _
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }
}
