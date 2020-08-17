package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.CreateColumnStatementProducer
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer
import com.github.starnowski.posmulten.postgresql.core.SetNotNullStatementProducer
import spock.lang.Specification
import spock.lang.Unroll

class SingleTenantColumnSQLDefinitionsProducerTest extends Specification {

    def tested = new SingleTenantColumnSQLDefinitionsProducer()

    @Unroll
    def "should create all required SQL definition for the tenant column"()
    {
        given:
            def createColumnStatementProducer = Mock(CreateColumnStatementProducer)
            def setDefaultStatementProducer = Mock(SetDefaultStatementProducer)
            def setNotNullStatementProducer = Mock(SetNotNullStatementProducer)
            tested.setCreateColumnStatementProducer(createColumnStatementProducer)
            tested.setSetDefaultStatementProducer(setDefaultStatementProducer)
            tested.setSetNotNullStatementProducer(setNotNullStatementProducer)

        when:


        then:
            tenantTable |   tenantColumn    |   defaultTenantColumn |   defaultTenantColumnType ||  expectedTenantColumn    |   expectedTenantColumnType
            "users"     |   "tenant_id"     |   "tenant"            |   "VARCHAR(255)"          ||  "tenant_id"             |   "VARCHAR(255)"

    }
}
