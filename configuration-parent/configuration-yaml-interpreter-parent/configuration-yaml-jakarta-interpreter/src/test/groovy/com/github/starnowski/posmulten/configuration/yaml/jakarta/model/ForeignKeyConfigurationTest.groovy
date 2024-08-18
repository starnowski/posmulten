package com.github.starnowski.posmulten.configuration.yaml.jakarta.model

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

class ForeignKeyConfigurationTest extends Specification {

    @Unroll
    def "equals method should return true for objects with same values [ob1 (#ob1), ob2 (#ob2)]"() {
        expect:
            ob1.equals(ob2) && ob2.equals(ob1)

        where:
            ob1                                                                                                                                    | ob2
            new ForeignKeyConfiguration()                                                                                                          | new ForeignKeyConfiguration()
            new ForeignKeyConfiguration().setConstraintName("constraint_x")                                                                        | new ForeignKeyConfiguration().setConstraintName("constraint_x")
            new ForeignKeyConfiguration().setTableName("tab_1")                                                                                    | new ForeignKeyConfiguration().setTableName("tab_1")
            new ForeignKeyConfiguration().setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("id", "tab_id").put("tab_uuid", "uuid").build()) | new ForeignKeyConfiguration().setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("id", "tab_id").put("tab_uuid", "uuid").build())
    }

    @Unroll
    def "hashCode method should return same result for objects with same values [ob1 (#ob1), ob2 (#ob2)]"() {
        expect:
            ob1.hashCode() == ob2.hashCode()

        where:
            ob1                                                                                                                                    | ob2
            new ForeignKeyConfiguration()                                                                                                          | new ForeignKeyConfiguration()
            new ForeignKeyConfiguration().setConstraintName("constraint_x")                                                                        | new ForeignKeyConfiguration().setConstraintName("constraint_x")
            new ForeignKeyConfiguration().setTableName("tab_1")                                                                                    | new ForeignKeyConfiguration().setTableName("tab_1")
            new ForeignKeyConfiguration().setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("id", "tab_id").put("tab_uuid", "uuid").build()) | new ForeignKeyConfiguration().setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("id", "tab_id").put("tab_uuid", "uuid").build())
    }

    @Unroll
    def "equals method should return false for objects with different values [ob1 (#ob1), ob2 (#ob2)]"() {
        expect:
            !ob1.equals(ob2) && !ob2.equals(ob1)

        where:
            ob1                                                                                                                                    | ob2
            new ForeignKeyConfiguration().setConstraintName("constraint_x")                                                                        | new ForeignKeyConfiguration().setConstraintName("constraint_y")
            new ForeignKeyConfiguration().setTableName("tab_1")                                                                                    | new ForeignKeyConfiguration().setTableName("tab_2")
            new ForeignKeyConfiguration().setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("id", "tab_id").put("tab_uuid", "uuid").build()) | new ForeignKeyConfiguration().setForeignKeyPrimaryKeyColumnsMappings(mapBuilder().put("id", "tab_id").put("tab_xuid", "uuid").build())

    }
}