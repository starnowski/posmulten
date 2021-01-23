package com.github.starnowski.posmulten.configuration.core.model

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

class RLSPolicyTest extends Specification {

    @Unroll
    def "equals method should return true for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.equals(ob2) && ob2.equals(ob1)

        where:
            ob1                                                                                                                                                                                     |   ob2
            new RLSPolicy()                                                                                                                                                                         |   new RLSPolicy()
            new RLSPolicy().setName("table_rls_policy")                                                                                                                                             |   new RLSPolicy().setName("table_rls_policy")
            new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(true)                                                                                                       |   new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(true)
            new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(false)                                                                                                      |   new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(false)
            new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")).setValidTenantValueConstraintName("t_valid")  |   new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")).setValidTenantValueConstraintName("t_valid")
            new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(true)                                                                                                                           |   new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(true)
            new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(false)                                                                                                                          |   new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(false)
            new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build()))              |   new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build()))

    }

    @Unroll
    def "hashCode method should return same result for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.hashCode() == ob2.hashCode()

        where:
            ob1                                                                                                                                                                                     |   ob2
            new RLSPolicy()                                                                                                                                                                         |   new RLSPolicy()
            new RLSPolicy().setName("table_rls_policy")                                                                                                                                             |   new RLSPolicy().setName("table_rls_policy")
            new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(true)                                                                                                       |   new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(true)
            new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(false)                                                                                                      |   new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(false)
            new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")).setValidTenantValueConstraintName("t_valid")  |   new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")).setValidTenantValueConstraintName("t_valid")
            new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(true)                                                                                                                           |   new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(true)
            new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(false)                                                                                                                          |   new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(false)
            new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build()))              |   new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build()))
    }

    @Unroll
    def "equals method should return false for objects with different values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            !ob1.equals(ob2) && !ob2.equals(ob1)

        where:
            ob1                                                                                                                                                                                     |   ob2
            new RLSPolicy().setName("table_rls_policy")                                                                                                                                             |   new RLSPolicy().setName("tab_rls_policy")
            new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(true)                                                                                                       |   new RLSPolicy().setTenantColumn("yyy_tenant").setCreateTenantColumnForTable(true)
            new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(false)                                                                                                      |   new RLSPolicy().setTenantColumn("xxx_tenant").setCreateTenantColumnForTable(true)
            new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")).setValidTenantValueConstraintName("t_valid")  |   new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_exists")).setValidTenantValueConstraintName("t_valid")
            new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")).setValidTenantValueConstraintName("t_valid")  |   new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")).setValidTenantValueConstraintName("tvalid")
            new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(true)                                                                                                                           |   new RLSPolicy().setSkipAddingOfTenantColumnDefaultValue(false)
            new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "VARCHAR(255)").put("record_uuid", "UUID").build()))        |   new RLSPolicy().setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build()))
    }
}
