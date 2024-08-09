package com.github.starnowski.posmulten.configuration.yaml.model

import spock.lang.Specification
import spock.lang.Unroll

class TableEntryTest extends Specification {

    @Unroll
    def "equals method should return true for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.equals(ob2) && ob2.equals(ob1)

        where:
            ob1                                                                                                 |   ob2
            new TableEntry()                                                                                    |   new TableEntry()
            new TableEntry().setName("table1")                                                                  |   new TableEntry().setName("table1")
            new TableEntry().setRlsPolicy(new RLSPolicy().setName("rls_test_policy"))                           |   new TableEntry().setRlsPolicy(new RLSPolicy().setName("rls_test_policy"))
            new TableEntry().setForeignKeys(Arrays.asList(new ForeignKeyConfiguration().setTableName("tab2")))  |   new TableEntry().setForeignKeys(Arrays.asList(new ForeignKeyConfiguration().setTableName("tab2")))
    }

    @Unroll
    def "hashCode method should return same result for objects with same values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            ob1.hashCode() == ob2.hashCode()

        where:
            ob1                                                                                                 |   ob2
            new TableEntry()                                                                                    |   new TableEntry()
            new TableEntry().setName("table1")                                                                  |   new TableEntry().setName("table1")
            new TableEntry().setRlsPolicy(new RLSPolicy().setName("rls_test_policy"))                           |   new TableEntry().setRlsPolicy(new RLSPolicy().setName("rls_test_policy"))
            new TableEntry().setForeignKeys(Arrays.asList(new ForeignKeyConfiguration().setTableName("tab2")))  |   new TableEntry().setForeignKeys(Arrays.asList(new ForeignKeyConfiguration().setTableName("tab2")))
    }

    @Unroll
    def "equals method should return false for objects with different values [ob1 (#ob1), ob2 (#ob2)]"()
    {
        expect:
            !ob1.equals(ob2) && !ob2.equals(ob1)

        where:
            ob1                                                                                                 |   ob2
            new TableEntry().setName("tableN")                                                                  |   new TableEntry().setName("table1")
            new TableEntry().setRlsPolicy(new RLSPolicy().setName("rls_test_policy"))                           |   new TableEntry().setRlsPolicy(new RLSPolicy().setName("rls_tab_policy"))
            new TableEntry().setForeignKeys(Arrays.asList(new ForeignKeyConfiguration().setTableName("tab2")))  |   new TableEntry().setForeignKeys(Arrays.asList(new ForeignKeyConfiguration().setTableName("tabX")))
    }
}
