package com.github.starnowski.posmulten.postgresql.core.context

import spock.lang.Specification

class DefaultTableColumnsTest extends Specification {

    def "should create object with empty map when passing null object as map"()
    {
        expect:
            new DefaultTableColumns("table", null).getIdentityColumnNameAndTypeMap().isEmpty()
    }
}
