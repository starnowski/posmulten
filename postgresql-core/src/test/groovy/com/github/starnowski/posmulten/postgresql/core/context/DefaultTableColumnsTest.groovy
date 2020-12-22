package com.github.starnowski.posmulten.postgresql.core.context

import spock.lang.Specification
import spock.lang.Unroll

class DefaultTableColumnsTest extends Specification {

    def "should create object with empty map when passing null object as map"()
    {
        expect:
            new DefaultTableColumns("table", null).getIdentityColumnNameAndTypeMap().isEmpty()
    }

    @Unroll
    def "constructor should create new map object based on passed object (#map)"()
    {
        given:
            def tested = new DefaultTableColumns("table", map)

        when:
            def result = tested.getIdentityColumnNameAndTypeMap()

        then:
            result == map
            !result.is(map)

        where:
            map << [[:], [id:"bigint"], [user_uid: "UUID", id:"bigint"]]
    }
}
