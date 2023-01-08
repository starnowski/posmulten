package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition
import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.postgresql.core.context.TableKey
import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder
import spock.lang.Unroll

class RLSPolicyConfigurationEnricherTest extends AbstractBaseTest {

    def tested = new RLSPolicyConfigurationEnricher()

    @Unroll
    def "should set builder component with rls policy for table name '#tableName', rls policy name '#rlsPolicyName', tenant column '#tenantColumn', primaryKeyColumnsNameToTypeMap '#primaryKeyColumnsNameToTypeMap', createTenantColumnForTable '#createTenantColumnForTable', nameForFunctionThatChecksIfRecordExistsInTable '#nameForFunctionThatChecksIfRecordExistsInTable', validTenantValueConstraintName '#validTenantValueConstraintName', skipAddingOfTenantColumnDefaultValue '#skipAddingOfTenantColumnDefaultValue'"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def entry = new TableEntry().setName(tableName)
                    .setRlsPolicy(new RLSPolicy()
                                    .setName(rlsPolicyName)
                                    .setTenantColumn(tenantColumn)
                                    .setPrimaryKeyDefinition(new PrimaryKeyDefinition()
                                            .setPrimaryKeyColumnsNameToTypeMap(primaryKeyColumnsNameToTypeMap)
                                            .setNameForFunctionThatChecksIfRecordExistsInTable(nameForFunctionThatChecksIfRecordExistsInTable))
                                    .setCreateTenantColumnForTable(createTenantColumnForTable)
                                    .setValidTenantValueConstraintName(validTenantValueConstraintName)
                                    .setSkipAddingOfTenantColumnDefaultValue(skipAddingOfTenantColumnDefaultValue))

        when:
            def result = tested.enrich(builder, entry)

        then:
            result == builder
            1 * builder.createRLSPolicyForTable(tableName, primaryKeyColumnsNameToTypeMap, tenantColumn, rlsPolicyName)
            1 * builder.setNameForFunctionThatChecksIfRecordExistsInTable(tableName, nameForFunctionThatChecksIfRecordExistsInTable)
            1 * builder.registerCustomValidTenantValueConstraintNameForTable(tableName, validTenantValueConstraintName)


        where:
            tableName   |   rlsPolicyName   |   tenantColumn    |   primaryKeyColumnsNameToTypeMap                                              |   createTenantColumnForTable  |   nameForFunctionThatChecksIfRecordExistsInTable  |   validTenantValueConstraintName  |   skipAddingOfTenantColumnDefaultValue
            "t1"        |   "rls_pol"       |   "tenant_co"     |   MapBuilder.mapBuilder().put("id", "bigint").build()                         |   false                       |   "is_record_exists"                              |   "table_ten_is_valid_con"        |   null
            "table1"    |   "table_rls_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build() |   null                        |   "is_table_exists"                               |   "tenant_table_shoul_be_valid"   |   false
    }

    @Unroll
    def "should set builder component with rls policy for table name '#tableName' in schema #tableSchema, rls policy name '#rlsPolicyName', tenant column '#tenantColumn', primaryKeyColumnsNameToTypeMap '#primaryKeyColumnsNameToTypeMap', createTenantColumnForTable '#createTenantColumnForTable', nameForFunctionThatChecksIfRecordExistsInTable '#nameForFunctionThatChecksIfRecordExistsInTable', validTenantValueConstraintName '#validTenantValueConstraintName', skipAddingOfTenantColumnDefaultValue '#skipAddingOfTenantColumnDefaultValue'"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def entry = new TableEntry()
                    .setName(tableName)
                    .setSchema(Optional.ofNullable(tableSchema))
                    .setRlsPolicy(new RLSPolicy()
                            .setName(rlsPolicyName)
                            .setTenantColumn(tenantColumn)
                            .setPrimaryKeyDefinition(new PrimaryKeyDefinition()
                                    .setPrimaryKeyColumnsNameToTypeMap(primaryKeyColumnsNameToTypeMap)
                                    .setNameForFunctionThatChecksIfRecordExistsInTable(nameForFunctionThatChecksIfRecordExistsInTable))
                            .setCreateTenantColumnForTable(createTenantColumnForTable)
                            .setValidTenantValueConstraintName(validTenantValueConstraintName)
                            .setSkipAddingOfTenantColumnDefaultValue(skipAddingOfTenantColumnDefaultValue))

        when:
            def result = tested.enrich(builder, entry)

        then:
            result == builder
            1 * builder.createRLSPolicyForTable(new TableKey(tableName, tableSchema), primaryKeyColumnsNameToTypeMap, tenantColumn, rlsPolicyName)
            1 * builder.setNameForFunctionThatChecksIfRecordExistsInTable(new TableKey(tableName, tableSchema), nameForFunctionThatChecksIfRecordExistsInTable)
            1 * builder.registerCustomValidTenantValueConstraintNameForTable(new TableKey(tableName, tableSchema), validTenantValueConstraintName)


        where:
            tableName   |   tableSchema         |   rlsPolicyName   |   tenantColumn    |   primaryKeyColumnsNameToTypeMap                                              |   createTenantColumnForTable  |   nameForFunctionThatChecksIfRecordExistsInTable  |   validTenantValueConstraintName  |   skipAddingOfTenantColumnDefaultValue
            "t1"        |   "some_schema"       |   "rls_pol"       |   "tenant_co"     |   MapBuilder.mapBuilder().put("id", "bigint").build()                         |   false                       |   "is_record_exists"                              |   "table_ten_is_valid_con"        |   null
            "table1"    |   "PROJECT_TEST"      |   "table_rls_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build() |   null                        |   "is_table_exists"                               |   "tenant_table_shoul_be_valid"   |   false
            "table5"    |   null                |   "table_rls_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build() |   null                        |   "is_table_exists"                               |   "tenant_table_shoul_be_valid"   |   false
    }

    @Unroll
    def "should invoke createTenantColumnForTable method when property createTenantColumnForTable is set with the 'true' value for table name '#tableName', rls policy name '#rlsPolicyName', tenant column '#tenantColumn', primaryKeyColumnsNameToTypeMap '#primaryKeyColumnsNameToTypeMap'"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def entry = new TableEntry().setName(tableName)
                    .setRlsPolicy(new RLSPolicy()
                            .setName(rlsPolicyName)
                            .setTenantColumn(tenantColumn)
                            .setPrimaryKeyDefinition(new PrimaryKeyDefinition()
                                    .setPrimaryKeyColumnsNameToTypeMap(primaryKeyColumnsNameToTypeMap))
                            .setCreateTenantColumnForTable(true))

        when:
            def result = tested.enrich(builder, entry)

        then:
            result == builder
            1 * builder.createRLSPolicyForTable(tableName, primaryKeyColumnsNameToTypeMap, tenantColumn, rlsPolicyName)
            1 * builder.createTenantColumnForTable(tableName)

        where:
            tableName   |   rlsPolicyName   |   tenantColumn    |   primaryKeyColumnsNameToTypeMap
            "t1"        |   "rls_pol"       |   "tenant_co"     |   MapBuilder.mapBuilder().put("user_id", "int").build()
            "table1"    |   "table_rls_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build()
    }

    @Unroll
    def "should invoke createTenantColumnForTable method when property createTenantColumnForTable is set with the 'true' value for table name '#tableName' in schema #tableSchema, rls policy name '#rlsPolicyName', tenant column '#tenantColumn', primaryKeyColumnsNameToTypeMap '#primaryKeyColumnsNameToTypeMap'"()
    {
        given:
        def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
        def entry = new TableEntry().setName(tableName)
                .setSchema(Optional.ofNullable(tableSchema))
                .setRlsPolicy(new RLSPolicy()
                        .setName(rlsPolicyName)
                        .setTenantColumn(tenantColumn)
                        .setPrimaryKeyDefinition(new PrimaryKeyDefinition()
                                .setPrimaryKeyColumnsNameToTypeMap(primaryKeyColumnsNameToTypeMap))
                        .setCreateTenantColumnForTable(true))

        when:
        def result = tested.enrich(builder, entry)

        then:
        result == builder
        1 * builder.createRLSPolicyForTable(new TableKey(tableName, tableSchema), primaryKeyColumnsNameToTypeMap, tenantColumn, rlsPolicyName)
        1 * builder.createTenantColumnForTable(new TableKey(tableName, tableSchema))

        where:
        tableName   |   tableSchema |   rlsPolicyName   |   tenantColumn    |   primaryKeyColumnsNameToTypeMap
        "t1"        |   "xxx"       |   "rls_pol"       |   "tenant_co"     |   MapBuilder.mapBuilder().put("user_id", "int").build()
        "table1"    |   "yyy"       |   "table_rls_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build()
        "table5"    |   null        |   "table_rls_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build()
    }

    @Unroll
    def "should invoke skipAddingOfTenantColumnDefaultValueForTable method when property skipAddingOfTenantColumnDefaultValue is set with the 'true' value for table name '#tableName', rls policy name '#rlsPolicyName', tenant column '#tenantColumn', primaryKeyColumnsNameToTypeMap '#primaryKeyColumnsNameToTypeMap'"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def entry = new TableEntry().setName(tableName)
                    .setRlsPolicy(new RLSPolicy()
                            .setName(rlsPolicyName)
                            .setTenantColumn(tenantColumn)
                            .setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(primaryKeyColumnsNameToTypeMap))
                            .setSkipAddingOfTenantColumnDefaultValue(true))

        when:
            def result = tested.enrich(builder, entry)

        then:
            result == builder
            1 * builder.createRLSPolicyForTable(tableName, primaryKeyColumnsNameToTypeMap, tenantColumn, rlsPolicyName)
            1 * builder.skipAddingOfTenantColumnDefaultValueForTable(tableName)

        where:
            tableName   |   rlsPolicyName   |   tenantColumn    |   primaryKeyColumnsNameToTypeMap
            "t1"        |   "rls_pol"       |   "col_for_ten"   |   MapBuilder.mapBuilder().put("user_id", "int").build()
            "comments"  |   "row_level_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build()
    }

    @Unroll
    def "should invoke skipAddingOfTenantColumnDefaultValueForTable method when property skipAddingOfTenantColumnDefaultValue is set with the 'true' value for table name '#tableName' in schema '#tableSchema', rls policy name '#rlsPolicyName', tenant column '#tenantColumn', primaryKeyColumnsNameToTypeMap '#primaryKeyColumnsNameToTypeMap'"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()
            def entry = new TableEntry().setName(tableName)
                    .setSchema(Optional.ofNullable(tableSchema))
                    .setRlsPolicy(new RLSPolicy()
                            .setName(rlsPolicyName)
                            .setTenantColumn(tenantColumn)
                            .setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(primaryKeyColumnsNameToTypeMap))
                            .setSkipAddingOfTenantColumnDefaultValue(true))

        when:
            def result = tested.enrich(builder, entry)

        then:
            result == builder
            1 * builder.createRLSPolicyForTable(new TableKey(tableName, tableSchema), primaryKeyColumnsNameToTypeMap, tenantColumn, rlsPolicyName)
            1 * builder.skipAddingOfTenantColumnDefaultValueForTable(new TableKey(tableName, tableSchema))

        where:
            tableName   |   tableSchema |   rlsPolicyName   |   tenantColumn    |   primaryKeyColumnsNameToTypeMap
            "t1"        |   "schema"    |   "rls_pol"       |   "col_for_ten"   |   MapBuilder.mapBuilder().put("user_id", "int").build()
            "comments"  |   null        |   "row_level_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build()
    }

    @Unroll
    def "should not invoke any builder's component method when invalid object is passed (#message)"()
    {
        given:
            def builder = prepareBuilderMockWithZeroExpectationOfMethodsInvocation()

        when:
            def result = tested.enrich(builder, entry)

        then:
            result == builder
            0 * builder._

        where:
            entry               |   message
            null                |   "null object"
            new TableEntry()    |   "table entry without rls policy object"
    }
}
