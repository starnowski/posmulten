package com.github.starnowski.posmulten.configuration.yaml.jakarta.model

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

class PrimaryKeyDefinitionTest extends Specification {

    @Unroll
    def "equals method should return true for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.equals(ob2) && ob2.equals(ob1)

        where:
            ob1                                                                                                                                             |   ob2
            new PrimaryKeyDefinition()                                                                                                                      |   new PrimaryKeyDefinition()
            new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")                                                |   new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")
            new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build())               |   new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build())

    }

    @Unroll
    def "hashCode method should return same result for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.hashCode() == ob2.hashCode()

        where:
            ob1                                                                                                                                     |   ob2
            new PrimaryKeyDefinition()                                                                                                              |   new PrimaryKeyDefinition()
            new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")                                        |   new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")
            new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build())       |   new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build())
    }

    @Unroll
    def "equals method should return false for objects with different values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            !ob1.equals(ob2) && !ob2.equals(ob1)

        where:
            ob1                                                                                                                                         |   ob2
            new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_record_exists")                                            |   new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("is_exists")
            new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "VARCHAR(255)").put("record_uuid", "UUID").build())     |   new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "bigint").put("record_uuid", "UUID").build())
    }
}
