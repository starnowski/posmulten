package com.github.starnowski.posmulten.configuration.yaml.jakarta.model

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.Arrays.asList

class SharedSchemaContextConfigurationTest extends Specification {

    @Unroll
    def "equals method should return true for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.equals(ob2) && ob2.equals(ob1)

        where:
            ob1                                                                                                     |   ob2
            new SharedSchemaContextConfiguration()                                                                  | new SharedSchemaContextConfiguration()
            new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx").setDefaultSchema("shema1") | new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx").setDefaultSchema("shema1")
            new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx")                            | new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx")
            new SharedSchemaContextConfiguration().setDefaultSchema("shema1")                                       | new SharedSchemaContextConfiguration().setDefaultSchema("shema1")
            new SharedSchemaContextConfiguration().setDefaultSchema("public")                                       | new SharedSchemaContextConfiguration().setDefaultSchema("public")
            new SharedSchemaContextConfiguration().setTables(asList(new TableEntry().setName("tab1")))              | new SharedSchemaContextConfiguration().setTables(asList(new TableEntry().setName("tab1")))
    }

    @Unroll
    def "hashCode method should return same result for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.hashCode() == ob2.hashCode()

        where:
            ob1                                                                                                     |   ob2
            new SharedSchemaContextConfiguration()                                                                  | new SharedSchemaContextConfiguration()
            new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx").setDefaultSchema("shema1") | new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx").setDefaultSchema("shema1")
            new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx")                            | new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx")
            new SharedSchemaContextConfiguration().setDefaultSchema("shema1")                                       | new SharedSchemaContextConfiguration().setDefaultSchema("shema1")
            new SharedSchemaContextConfiguration().setDefaultSchema("public")                                       | new SharedSchemaContextConfiguration().setDefaultSchema("public")
            new SharedSchemaContextConfiguration().setTables(asList(new TableEntry().setName("tab1")))              | new SharedSchemaContextConfiguration().setTables(asList(new TableEntry().setName("tab1")))
    }

    @Unroll
    def "equals method should return false for objects with different values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            !ob1.equals(ob2) && !ob2.equals(ob1)

        where:
            ob1 |   ob2
            new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx").setDefaultSchema("shema1") | new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx").setDefaultSchema("public")
            new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("xxx") | new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("yyy")
            new SharedSchemaContextConfiguration().setDefaultSchema("shema1") | new SharedSchemaContextConfiguration().setDefaultSchema("public")
            new SharedSchemaContextConfiguration().setDefaultSchema("public") | new SharedSchemaContextConfiguration().setDefaultSchema("non_public")
    }
}
