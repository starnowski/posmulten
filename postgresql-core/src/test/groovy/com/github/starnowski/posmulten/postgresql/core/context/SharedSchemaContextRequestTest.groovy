package com.github.starnowski.posmulten.postgresql.core.context

import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder
import spock.lang.Specification
import spock.lang.Unroll

class SharedSchemaContextRequestTest extends Specification {

    @Unroll
    def "should resolve expected tenant column #expectedTenantColumn for table key #tableKey for default tenant column #defaultColumn and tables configuration #tablesConfigsMap"() {
        given:
            def tested = new SharedSchemaContextRequest()
            tested.setDefaultTenantIdColumn(defaultColumn)
            tested.getTableColumnsList().putAll(tablesConfigsMap)

        when:
            def result = tested.resolveTenantColumnByTableKey(tableKey)

        then:
            result == expectedTenantColumn

        where:
            tableKey                        |   defaultColumn   |   tablesConfigsMap                                                                                                                                                                            ||  expectedTenantColumn
            new TableKey("users", null)     |   "tenant"        |   MapBuilder.mapBuilder().put(new TableKey("users", null), new DefaultTableColumns(null, null)).build()                                                                                       ||  "tenant"
            new TableKey("users", null)     |   "ten_id"        |   MapBuilder.mapBuilder().put(new TableKey("users", null), new DefaultTableColumns(null, null)).build()                                                                                       ||  "ten_id"
            new TableKey("users", null)     |   "ten_id"        |   MapBuilder.mapBuilder().put(new TableKey("users", null), new DefaultTableColumns("ten_col", null)).build()                                                                                  ||  "ten_col"
            new TableKey("users", null)     |   "tt"            |   MapBuilder.mapBuilder().put(new TableKey("users", "public"), new DefaultTableColumns("col", null)).put(new TableKey("users", null), new DefaultTableColumns(null, null)).build()            ||  "tt"
            new TableKey("users", "sch_1")  |   "tt"            |   MapBuilder.mapBuilder().put(new TableKey("users", "public"), new DefaultTableColumns("col", null)).put(new TableKey("users", "sch_1"), new DefaultTableColumns("col_for_t", null)).build()  ||  "col_for_t"
    }


    def "should return null when table key is not registed"() {
        given:
            def tested = new SharedSchemaContextRequest()
            tested.setDefaultTenantIdColumn("tenant")
            tested.getTableColumnsList().putAll(MapBuilder.mapBuilder()
                    .put(new TableKey("users", "public"), new DefaultTableColumns("col", null))
                    .put(new TableKey("posts", "sch_1"), new DefaultTableColumns("col_for_t", null)).build())

        when:
            def result = tested.resolveTenantColumnByTableKey(new TableKey("comments", "public"))

        then:
            result == null
    }
}
