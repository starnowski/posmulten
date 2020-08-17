package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.CreateColumnStatementProducer
import com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer
import com.github.starnowski.posmulten.postgresql.core.SetNotNullStatementProducer
import groovy.model.DefaultTableColumn
import spock.lang.Specification
import spock.lang.Unroll

class SingleTenantColumnSQLDefinitionsProducerTest extends Specification {

    def tested = new SingleTenantColumnSQLDefinitionsProducer()

    @Unroll
    def "should create all required SQL definition for the tenant column in table #tenantTable, tenant column: #tenantColumn,  default tenant column: #defaultTenantColumn, default tenant column type: #defaultTenantColumnType"()
    {
        given:
            def createColumnStatementProducer = Mock(CreateColumnStatementProducer)
            def setDefaultStatementProducer = Mock(SetDefaultStatementProducer)
            def setNotNullStatementProducer = Mock(SetNotNullStatementProducer)
            tested.setCreateColumnStatementProducer(createColumnStatementProducer)
            tested.setSetDefaultStatementProducer(setDefaultStatementProducer)
            tested.setSetNotNullStatementProducer(setNotNullStatementProducer)
            def tableColumns = dtc(tenantColumn)

        when:
            def results = tested.produce(tenantTable, tableColumns, defaultTenantColumn, defaultTenantColumnType)

        then:


        where:
            tenantTable             |   tenantColumn    |   defaultTenantColumn |   defaultTenantColumnType ||  expectedTenantColumn    |   expectedTenantColumnType
            tk("users", null)       |   "tenant_id"     |   "tenant"            |   "VARCHAR(255)"          ||  "tenant_id"             |   "VARCHAR(255)"

    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }

    DefaultTableColumns dtc(table)
    {
        new DefaultTableColumn(table, null)
    }
}
