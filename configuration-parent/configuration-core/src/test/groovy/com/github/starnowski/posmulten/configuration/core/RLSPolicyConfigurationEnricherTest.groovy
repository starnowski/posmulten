package com.github.starnowski.posmulten.configuration.core

import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
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
                                    .setPrimaryKeyColumnsNameToTypeMap(primaryKeyColumnsNameToTypeMap)
                                    .setCreateTenantColumnForTable(createTenantColumnForTable)
                                    .setNameForFunctionThatChecksIfRecordExistsInTable(nameForFunctionThatChecksIfRecordExistsInTable)
                                    .setValidTenantValueConstraintName(validTenantValueConstraintName)
                                    .setSkipAddingOfTenantColumnDefaultValue(skipAddingOfTenantColumnDefaultValue))

        when:
            def result = tested.enrich(builder, entry)

        then:
            result == builder
            1 * builder.createRLSPolicyForTable(tableName, primaryKeyColumnsNameToTypeMap, tenantColumn, rlsPolicyName)
            1 * builder.setNameForFunctionThatChecksIfRecordExistsInTable(tableName, nameForFunctionThatChecksIfRecordExistsInTable)
            1 * builder.registerCustomValidTenantValueConstraintNameForTable(tableName, validTenantValueConstraintName)
//        1 * builder.createTenantColumnForTable(tableName)
//        1 * builder.skipAddingOfTenantColumnDefaultValueForTable(tableName)


        where:
            tableName   |   rlsPolicyName   |   tenantColumn    |   primaryKeyColumnsNameToTypeMap                                              |   createTenantColumnForTable  |   nameForFunctionThatChecksIfRecordExistsInTable  |   validTenantValueConstraintName  |   skipAddingOfTenantColumnDefaultValue
            "t1"        |   "rls_pol"       |   "tenant_co"     |   MapBuilder.mapBuilder().put("id", "bigint").build()                         |   false                       |   "is_record_exists"                              |   "table_ten_is_valid_con"        |   null
            "table1"    |   "table_rls_pol" |   "ten_id"        |   MapBuilder.mapBuilder().put("tab_id", "bigint").put("uuid", "UUID").build() |   null                        |   "is_table_exists"                               |   "tenant_table_shoul_be_valid"   |   false
    }

}
