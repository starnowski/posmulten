package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.CreateColumnStatementProducer
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer
import com.github.starnowski.posmulten.postgresql.core.SetNotNullStatementProducer
import spock.lang.Specification

class SingleTenantColumnSQLDefinitionsProducerTest extends Specification {

    def tested = new SingleTenantColumnSQLDefinitionsProducer()

    def ""()
    {
        def createColumnStatementProducer = Mock(CreateColumnStatementProducer)
        def setDefaultStatementProducer = Mock(SetDefaultStatementProducer)
        def setNotNullStatementProducer = Mock(SetNotNullStatementProducer)
        tested.setCreateColumnStatementProducer(createColumnStatementProducer)
        tested.setSetDefaultStatementProducer(setDefaultStatementProducer)
        tested.setSetNotNullStatementProducer(setNotNullStatementProducer)
    }
}
