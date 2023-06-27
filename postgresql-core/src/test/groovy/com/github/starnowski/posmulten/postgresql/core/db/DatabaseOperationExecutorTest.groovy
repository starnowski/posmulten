package com.github.starnowski.posmulten.postgresql.core.db

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.db.operations.CreateOperationsProcessor
import com.github.starnowski.posmulten.postgresql.core.db.operations.DatabaseOperationsLoggerProcessor
import com.github.starnowski.posmulten.postgresql.core.db.operations.DropOperationsProcessor
import com.github.starnowski.posmulten.postgresql.core.db.operations.IDatabaseOperationsProcessor
import com.github.starnowski.posmulten.postgresql.core.db.operations.ValidateOperationsProcessor
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource
import java.sql.SQLException
import java.util.stream.Collectors

class DatabaseOperationExecutorTest extends Specification {

    def"should invoke correct operation processor for CREATE operation"()
    {
        given:
            def createOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def dropOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def validateOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def databaseOperationsLoggerProcessor = Mock(DatabaseOperationsLoggerProcessor)
            Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap = new HashMap<>();
            operationsProcessorMap.put(DatabaseOperationType.DROP, dropOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.CREATE, createOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.VALIDATE, validateOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.LOG_ALL, databaseOperationsLoggerProcessor)
            def tested = new DatabaseOperationExecutor(operationsProcessorMap)
            DataSource dataSource = Mock(DataSource)
            List<SQLDefinition> sqlDefinitions = new ArrayList<>()

        when:
            tested.execute(dataSource, sqlDefinitions, DatabaseOperationType.CREATE)

        then:
            1 * createOperationProcessor.run(dataSource, sqlDefinitions)

        and: "no other processor should be invoked"
            0 * dropOperationProcessor.run(dataSource, sqlDefinitions)
            0 * validateOperationProcessor.run(dataSource, sqlDefinitions)
            0 * databaseOperationsLoggerProcessor.run(dataSource, sqlDefinitions)
    }

    def"should invoke correct operation processor for DROP operation"()
    {
        given:
            def createOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def dropOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def validateOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def databaseOperationsLoggerProcessor = Mock(DatabaseOperationsLoggerProcessor)
            Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap = new HashMap<>();
            operationsProcessorMap.put(DatabaseOperationType.DROP, dropOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.CREATE, createOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.VALIDATE, validateOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.LOG_ALL, databaseOperationsLoggerProcessor)
            def tested = new DatabaseOperationExecutor(operationsProcessorMap)
            DataSource dataSource = Mock(DataSource)
            List<SQLDefinition> sqlDefinitions = new ArrayList<>()

        when:
            tested.execute(dataSource, sqlDefinitions, DatabaseOperationType.DROP)

        then:
            1 * dropOperationProcessor.run(dataSource, sqlDefinitions)

        and: "no other processor should be invoked"
            0 * createOperationProcessor.run(dataSource, sqlDefinitions)
            0 * validateOperationProcessor.run(dataSource, sqlDefinitions)
            0 * databaseOperationsLoggerProcessor.run(dataSource, sqlDefinitions)
    }

    def"should invoke correct operation processor for VALIDATE operation"()
    {
        given:
            def createOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def dropOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def validateOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def databaseOperationsLoggerProcessor = Mock(DatabaseOperationsLoggerProcessor)
            Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap = new HashMap<>();
            operationsProcessorMap.put(DatabaseOperationType.DROP, dropOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.CREATE, createOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.VALIDATE, validateOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.LOG_ALL, databaseOperationsLoggerProcessor)
            def tested = new DatabaseOperationExecutor(operationsProcessorMap)
            DataSource dataSource = Mock(DataSource)
            List<SQLDefinition> sqlDefinitions = new ArrayList<>()

        when:
            tested.execute(dataSource, sqlDefinitions, DatabaseOperationType.VALIDATE)

        then:
            1 * validateOperationProcessor.run(dataSource, sqlDefinitions)

        and: "no other processor should be invoked"
            0 * createOperationProcessor.run(dataSource, sqlDefinitions)
            0 * dropOperationProcessor.run(dataSource, sqlDefinitions)
            0 * databaseOperationsLoggerProcessor.run(dataSource, sqlDefinitions)
    }

    def"should invoke correct operation processor for LOG_ALL operation"()
    {
        given:
            def createOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def dropOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def validateOperationProcessor = Mock(IDatabaseOperationsProcessor)
            def databaseOperationsLoggerProcessor = Mock(DatabaseOperationsLoggerProcessor)
            Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap = new HashMap<>();
            operationsProcessorMap.put(DatabaseOperationType.DROP, dropOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.CREATE, createOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.VALIDATE, validateOperationProcessor)
            operationsProcessorMap.put(DatabaseOperationType.LOG_ALL, databaseOperationsLoggerProcessor)
            def tested = new DatabaseOperationExecutor(operationsProcessorMap)
            DataSource dataSource = Mock(DataSource)
            List<SQLDefinition> sqlDefinitions = new ArrayList<>()

        when:
            tested.execute(dataSource, sqlDefinitions, DatabaseOperationType.LOG_ALL)

        then:
            1 * databaseOperationsLoggerProcessor.run(dataSource, sqlDefinitions)

        and: "no other processor should be invoked"
            0 * createOperationProcessor.run(dataSource, sqlDefinitions)
            0 * dropOperationProcessor.run(dataSource, sqlDefinitions)
            0 * validateOperationProcessor.run(dataSource, sqlDefinitions)
    }

    def "should have expected list of operation processor" (){
        given:
            def tested = new DatabaseOperationExecutor()

        when:
            def results = tested.getOperationsProcessorMap()

        then:
            results.values().stream().map({it -> it.getClass()}).collect(Collectors.toSet()) == new HashSet([CreateOperationsProcessor, DropOperationsProcessor, ValidateOperationsProcessor, DatabaseOperationsLoggerProcessor])
    }

    @Unroll
    def"should rethrow exception [#exception] thrown by database operation processor"()
    {
        given:
            def createOperationProcessor = Mock(IDatabaseOperationsProcessor)
            Map<DatabaseOperationType, IDatabaseOperationsProcessor> operationsProcessorMap = new HashMap<>()
            operationsProcessorMap.put(DatabaseOperationType.CREATE, createOperationProcessor)
            def tested = new DatabaseOperationExecutor(operationsProcessorMap)
            DataSource dataSource = Mock(DataSource)
            List<SQLDefinition> sqlDefinitions = new ArrayList<>()

        when:
            tested.execute(dataSource, sqlDefinitions, DatabaseOperationType.CREATE)

        then:
            1 * createOperationProcessor.run(dataSource, sqlDefinitions) >> { throw exception }
            def ex = thrown(exception.getClass())
            ex.is(exception)

        where:
            exception << [new SQLException(), new ValidationDatabaseOperationsException(new HashMap<String, Set<String>>())]
    }
}
