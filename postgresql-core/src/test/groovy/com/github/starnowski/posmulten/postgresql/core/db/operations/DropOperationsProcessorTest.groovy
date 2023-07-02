package com.github.starnowski.posmulten.postgresql.core.db.operations

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

class DropOperationsProcessorTest extends Specification {

    @Unroll
    def "should run drop scripts"(){
        given:
            def tested = new DropOperationsProcessor()
            def dataSource = Mock(DataSource)
            def connection = Mock(Connection)
            def statement = Mock(Statement)
            dataSource.getConnection() >> connection
            connection.createStatement() >> statement
            LinkedList<SQLDefinition> stack = new LinkedList<>()

        when:
            tested.run(dataSource, definitions)

        then:
            definitions.forEach({it -> stack.push(it)})
            stack.stream().forEach({it ->
                1 * statement.execute(it.getDropScript())
            })

        where:
            definitions << [[sqlDef("cre1"), sqlDef("cre2")], [sqlDef("creX"), sqlDef("creY"), sqlDef("creZ")]]
    }

    @Unroll
    def "should run drop scripts for connection"(){
        given:
            def tested = new DropOperationsProcessor()
            def connection = Mock(Connection)
            def statement = Mock(Statement)
            connection.createStatement() >> statement
            LinkedList<SQLDefinition> stack = new LinkedList<>()

        when:
            tested.run(connection, definitions)

        then:
            definitions.forEach({it -> stack.push(it)})
            stack.stream().forEach({it ->
                1 * statement.execute(it.getDropScript())
            })

        where:
            definitions << [[sqlDef("cre1"), sqlDef("cre2")], [sqlDef("creX"), sqlDef("creY"), sqlDef("creZ")]]
    }

    @Unroll
    def "should rethrow exception thrown during executing drop scripts"(){
        given:
            def tested = new DropOperationsProcessor()
            def dataSource = Mock(DataSource)
            def connection = Mock(Connection)
            def statement = Mock(Statement)
            dataSource.getConnection() >> connection
            connection.createStatement() >> statement
            dsThatThrowsExceptions.forEach({it ->
                statement.execute(it) >> { throw new SQLException()}
            })

        when:
            tested.run(dataSource, definitions)

        then:
            thrown(SQLException)

        where:
            definitions                                         |   dsThatThrowsExceptions
            [sqlDef("cre1"), sqlDef("cre2")]                    |   ["cre1"]
            [sqlDef("creX"), sqlDef("creY"), sqlDef("creZ")]    |   ["creZ"]
    }

    @Unroll
    def "should rethrow exception thrown during executing drop scripts for connection"(){
        given:
            def tested = new DropOperationsProcessor()
            def connection = Mock(Connection)
            def statement = Mock(Statement)
            connection.createStatement() >> statement
            dsThatThrowsExceptions.forEach({it ->
                statement.execute(it) >> { throw new SQLException()}
            })

        when:
            tested.run(connection, definitions)

        then:
            thrown(SQLException)

        where:
            definitions                                         |   dsThatThrowsExceptions
            [sqlDef("cre1"), sqlDef("cre2")]                    |   ["cre1"]
            [sqlDef("creX"), sqlDef("creY"), sqlDef("creZ")]    |   ["creZ"]
    }

    private static SQLDefinition sqlDef(String dropScrip){
        SQLDefinition definition = Mockito.mock(SQLDefinition)
        Mockito.when(definition.getDropScript()).thenReturn(dropScrip)
        definition
    }
}
