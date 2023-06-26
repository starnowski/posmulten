package com.github.starnowski.posmulten.postgresql.core.db.operations.util

import spock.lang.Specification
import spock.lang.Unroll

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class SQLUtilTest extends Specification {
    @Unroll
    def "should return result #expectedResult returned by prepared statement"(){
        given:
            def rs = Mock(ResultSet)
            def ps = Mock(PreparedStatement)
            def con = Mock(Connection)
            con.prepareStatement(query) >> ps
            ps.executeQuery() >> rs
            def tested = new SQLUtil()

        when:
            def result = tested.returnLongResultForQuery(con, query)

        then:
            result == expectedResult
            1 * rs.next()
            1 * rs.getLong(1) >> expectedResult

        where:
            query           ||  expectedResult
            "Some select"   ||  1
            "SELECT 32"     ||  137
            "SELECT *"      ||  -1
    }
}
