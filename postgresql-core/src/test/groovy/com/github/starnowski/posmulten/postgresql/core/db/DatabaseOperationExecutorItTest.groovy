package com.github.starnowski.posmulten.postgresql.core.db

import com.github.starnowski.posmulten.postgresql.core.TestApplication
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext
import com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory
import com.github.starnowski.posmulten.postgresql.core.db.operations.exceptions.ValidationDatabaseOperationsException
import com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

@SpringBootTest(classes = [TestApplication.class])
class DatabaseOperationExecutorItTest extends Specification {

    def tested = new DatabaseOperationExecutor()
    @Autowired
    DataSource dataSource
    @Shared
    def defaultSchemaContext = new DefaultSharedSchemaContextBuilder("{{db_schema}}").setGrantee("{{db_rls_policy_user}}").build()
    @Shared
    def decoratorContext = DefaultDecoratorContext.builder().withReplaceCharactersMap(MapBuilder.mapBuilder()
            .put("{{db_schema}}", "non_public_schema")
            .put("{{db_rls_policy_user}}", "postgresql-core-user")
            .build()).build()
    @Shared
    List<SQLDefinition> definitions = new SharedSchemaContextDecoratorFactory().build(defaultSchemaContext, decoratorContext).getSqlDefinitions()

    def "test 1: check statements should failed"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.VALIDATE)

        then:
            thrown(ValidationDatabaseOperationsException)
    }

    def "test 2: execution of creation scripts should be successful"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.CREATE)

        then:
            noExceptionThrown()
    }

    def "test 3: check statements should pass"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.VALIDATE)

        then:
            noExceptionThrown()
    }

    def "test 4: logging should pass"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.LOG_ALL)

        then:
            noExceptionThrown()
    }

    def "test 5: drop scripts should pass"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.DROP)

        then:
            noExceptionThrown()
    }

    def "test 6: check statements should failed"()
    {
        when:
            tested.execute(dataSource, definitions, DatabaseOperationType.VALIDATE)

        then:
            thrown(ValidationDatabaseOperationsException)
    }
}
