package com.github.starnowski.posmulten.postgresql.core.db.operations

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

class CreateOperationsProcessorTest extends Specification {

    @Unroll
    def "should run creation scripts"(){
        given:
            def tested = new CreateOperationsProcessor()
            def dataSource = Mock(DataSource)
            def connection = Mock(Connection)
            def statement = Mock(Statement)
            dataSource.getConnection() >> connection
            connection.createStatement() >> statement

        when:
            tested.run(dataSource, definitions)

        then:
            definitions.forEach({it ->
                1 * statement.execute(it.getCreateScript())
            })

        where:
            definitions << [[sqlDef("cre1"), sqlDef("cre2")], [sqlDef("creX"), sqlDef("creY"), sqlDef("creZ")]]
    }

    @Unroll
    def "should run creation scripts for connection"(){
        given:
            def tested = new CreateOperationsProcessor()
            def connection = Mock(Connection)
            def statement = Mock(Statement)
            connection.createStatement() >> statement

        when:
            tested.run(connection, definitions)

        then:
            definitions.forEach({it ->
                1 * statement.execute(it.getCreateScript())
            })

        where:
            definitions << [[sqlDef("cre1"), sqlDef("cre2")], [sqlDef("creX"), sqlDef("creY"), sqlDef("creZ")]]
    }

    @Unroll
    def "should rethrow exception thrown during executing creation scripts"(){
        given:
            def tested = new CreateOperationsProcessor()
            def dataSource = Mock(DataSource)
            def connection = Mock(Connection)
            def statement = Mock(Statement)
            dataSource.getConnection() >> connection
            connection.createStatement() >> statement
            csThatThrowsExceptions.forEach({it ->
                statement.execute(it) >> { throw new SQLException()}
            })

        when:
            tested.run(dataSource, definitions)

        then:
            thrown(SQLException)

        where:
            definitions                                         |   csThatThrowsExceptions
            [sqlDef("cre1"), sqlDef("cre2")]                    |   ["cre1"]
            [sqlDef("creX"), sqlDef("creY"), sqlDef("creZ")]    |   ["creZ"]
    }

    @Unroll
    def "should rethrow exception thrown during executing creation scripts for connection"(){
        given:
            def tested = new CreateOperationsProcessor()
            def connection = Mock(Connection)
            def statement = Mock(Statement)
            connection.createStatement() >> statement
            csThatThrowsExceptions.forEach({it ->
                statement.execute(it) >> { throw new SQLException()}
            })

        when:
            tested.run(connection, definitions)

        then:
            thrown(SQLException)

        where:
            definitions                                         |   csThatThrowsExceptions
            [sqlDef("cre1"), sqlDef("cre2")]                    |   ["cre1"]
            [sqlDef("creX"), sqlDef("creY"), sqlDef("creZ")]    |   ["creZ"]
    }

    private static SQLDefinition sqlDef(String createScript){
        SQLDefinition definition = Mockito.mock(SQLDefinition)
        Mockito.when(definition.getCreateScript()).thenReturn(createScript)
        definition
    }

}
