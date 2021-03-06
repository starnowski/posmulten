package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.core.*
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import spock.lang.Specification
import spock.lang.Unroll

class SingleTenantColumnSQLDefinitionsProducerTest extends Specification {

    def tested = new SingleTenantColumnSQLDefinitionsProducer()

    @Unroll
    def "should create all required SQL definition for the tenant column in table #tenantTable, tenant column: #tenantColumn,  default tenant column: #defaultTenantColumn, default tenant column type: #defaultTenantColumnType"()
    {
        given:
            def createColumnStatementProducer = Mock(CreateColumnStatementProducer)
            def setNotNullStatementProducer = Mock(SetNotNullStatementProducer)
            def createColumnStatementProducerSQLDefinition = Mock(SQLDefinition)
            def setNotNullStatementProducerSQLDefinition = Mock(SQLDefinition)
            def capturedCreateColumnStatementProducerParameters = null
            def capturedSetNotNullStatementProducerParameters = null
            tested.setCreateColumnStatementProducer(createColumnStatementProducer)
            tested.setSetNotNullStatementProducer(setNotNullStatementProducer)
            def tableColumns = dtc(tenantColumn)

        when:
            def results = tested.produce(tenantTable, tableColumns, defaultTenantColumn, defaultTenantColumnType)

        then:
            1 * createColumnStatementProducer.produce(_) >>  {
                parameters ->
                    capturedCreateColumnStatementProducerParameters = parameters[0]
                    createColumnStatementProducerSQLDefinition
            }
            results.contains(createColumnStatementProducerSQLDefinition)
            CreateColumnStatementProducerParameters columnStatementProducerParameters = capturedCreateColumnStatementProducerParameters
            columnStatementProducerParameters.getTable() == tenantTable.getTable()
            columnStatementProducerParameters.getSchema() == tenantTable.getSchema()
            columnStatementProducerParameters.getColumn() == expectedTenantColumn
            columnStatementProducerParameters.getColumnType() == expectedTenantColumnType

        then:
            1 * setNotNullStatementProducer.produce(_) >>  {
                parameters ->
                    capturedSetNotNullStatementProducerParameters = parameters[0]
                    setNotNullStatementProducerSQLDefinition
            }
            results.contains(setNotNullStatementProducerSQLDefinition)
            ISetNotNullStatementProducerParameters iSetNotNullStatementProducerParameters = capturedSetNotNullStatementProducerParameters
            iSetNotNullStatementProducerParameters.getTable() == tenantTable.getTable()
            iSetNotNullStatementProducerParameters.getSchema() == tenantTable.getSchema()
            iSetNotNullStatementProducerParameters.getColumn() == expectedTenantColumn

        and: "pass SQL definition in correct order"
            results == [createColumnStatementProducerSQLDefinition, setNotNullStatementProducerSQLDefinition]

        where:
            tenantTable                     |   tenantColumn    |   defaultTenantColumn |   defaultTenantColumnType ||  expectedTenantColumn    |   expectedTenantColumnType
            tk("users", null)               |   "tenant_id"     |   "tenant"            |   "VARCHAR(255)"          ||  "tenant_id"             |   "VARCHAR(255)"
            tk("users", "public")           |   "tenant_id"     |   "tenant"            |   "text"                  ||  "tenant_id"             |   "text"
            tk("users", "other_schema")     |   "tenant_id"     |   "tenant_col"        |   "VARCHAR(255)"          ||  "tenant_id"             |   "VARCHAR(255)"
            tk("some_tab", "other_schema")  |   "ten"           |   "tenant"            |   "VARCHAR(32)"           ||  "ten"                   |   "VARCHAR(32)"
            tk("some_tab", "other_schema")  |   null            |   "tenant_col"        |   "VARCHAR(255)"          |  "tenant_col"            |   "VARCHAR(255)"
    }

    TableKey tk(String table, String schema)
    {
        new TableKey(table, schema)
    }

    DefaultTableColumns dtc(table)
    {
        new DefaultTableColumns(table, [:])
    }
}
