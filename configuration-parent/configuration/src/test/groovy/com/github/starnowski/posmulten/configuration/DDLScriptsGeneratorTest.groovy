package com.github.starnowski.posmulten.configuration

import com.github.starnowski.posmulten.configuration.core.context.DDLWriter
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext
import com.github.starnowski.posmulten.postgresql.core.context.decorator.ISharedSchemaContextDecorator
import com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory
import spock.lang.Specification
import spock.lang.Unroll

class DDLScriptsGeneratorTest extends Specification {

    @Unroll
    def "should create DDL scripts for file #configFilePath and save creation script in file #creationScriptPath and dropping script in file #droppingScriptPath and file with checking statements #checkinStatementsPath"()
    {
        given:
            def defaultSharedSchemaContextBuilderFactoryResolver = Mock(DefaultSharedSchemaContextBuilderFactoryResolver)
            def ddlWriter = Mock(DDLWriter)
            def tested = new DDLScriptsGenerator(defaultSharedSchemaContextBuilderFactoryResolver, ddlWriter)
            IDefaultSharedSchemaContextBuilderFactory defaultSharedSchemaContextBuilderFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
            DefaultSharedSchemaContextBuilder builder = Mock(DefaultSharedSchemaContextBuilder)
            ISharedSchemaContext context = Mock(ISharedSchemaContext)

        when:
            tested.generate(configFilePath, creationScriptPath, droppingScriptPath, checkinStatementsPath)

        then:
            1 * defaultSharedSchemaContextBuilderFactoryResolver.resolve(configFilePath) >> defaultSharedSchemaContextBuilderFactory
            1 * defaultSharedSchemaContextBuilderFactory.build(configFilePath) >> builder
            1 * builder.build() >> context
            1 * ddlWriter.saveCreteScripts(creationScriptPath, context)
            1 * ddlWriter.saveDropScripts(droppingScriptPath, context)
            1 * ddlWriter.saveCheckingStatements(checkinStatementsPath, context)

        where:
            configFilePath                  |   creationScriptPath              |   droppingScriptPath          |   checkinStatementsPath
            "C:\\some\\path\\config.yml"    |   "Z:\\create-shared-schema.sql"  |   ".\\drop-shared-schema.sql" |   "X:\\XXX\\checking\\sanitary_check.sql"
            "dir/cofing.xml"                |   "create-schema.sql"             |   "drop-schema.sql"           |   "check.sql"
    }

    @Unroll
    def "should create DDL scripts and pass shared schema context decorator to writers"()
    {
        given:
            def defaultSharedSchemaContextBuilderFactoryResolver = Mock(DefaultSharedSchemaContextBuilderFactoryResolver)
            def ddlWriter = Mock(DDLWriter)
            def schemaContextDecoratorFactory = Mock(SharedSchemaContextDecoratorFactory)
            def tested = new DDLScriptsGenerator(defaultSharedSchemaContextBuilderFactoryResolver, ddlWriter, schemaContextDecoratorFactory)
            IDefaultSharedSchemaContextBuilderFactory defaultSharedSchemaContextBuilderFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
            DefaultSharedSchemaContextBuilder builder = Mock(DefaultSharedSchemaContextBuilder)
            ISharedSchemaContext context = Mock(ISharedSchemaContext)
            ISharedSchemaContextDecorator contextDecorator = Mock(ISharedSchemaContextDecorator)
            DefaultDecoratorContext decoratorContext = Mock(DefaultDecoratorContext)

        when:
            tested.generate(configFilePath, creationScriptPath, droppingScriptPath, checkinStatementsPath, decoratorContext)

        then:
            1 * defaultSharedSchemaContextBuilderFactoryResolver.resolve(configFilePath) >> defaultSharedSchemaContextBuilderFactory
            1 * defaultSharedSchemaContextBuilderFactory.build(configFilePath) >> builder
            1 * builder.build() >> context
            1 * schemaContextDecoratorFactory.build(context, decoratorContext) >> contextDecorator
            1 * ddlWriter.saveCreteScripts(creationScriptPath, contextDecorator)
            1 * ddlWriter.saveDropScripts(droppingScriptPath, contextDecorator)
            1 * ddlWriter.saveCheckingStatements(checkinStatementsPath, contextDecorator)

        where:
            configFilePath                  |   creationScriptPath              |   droppingScriptPath          |   checkinStatementsPath
            "C:\\some\\path\\config.yml"    |   "Z:\\create-shared-schema.sql"  |   ".\\drop-shared-schema.sql" |   "X:\\XXX\\checking\\sanitary_check.sql"
    }

    @Unroll
    def "should create DDL scripts for file #configFilePath and save creation script in file #creationScriptPath but do not save dropping script when file is not specified"()
    {
        given:
            def defaultSharedSchemaContextBuilderFactoryResolver = Mock(DefaultSharedSchemaContextBuilderFactoryResolver)
            def ddlWriter = Mock(DDLWriter)
            def tested = new DDLScriptsGenerator(defaultSharedSchemaContextBuilderFactoryResolver, ddlWriter)
            IDefaultSharedSchemaContextBuilderFactory defaultSharedSchemaContextBuilderFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
            DefaultSharedSchemaContextBuilder builder = Mock(DefaultSharedSchemaContextBuilder)
            ISharedSchemaContext context = Mock(ISharedSchemaContext)

        when:
            tested.generate(configFilePath, creationScriptPath, null, null)

        then:
            1 * defaultSharedSchemaContextBuilderFactoryResolver.resolve(configFilePath) >> defaultSharedSchemaContextBuilderFactory
            1 * defaultSharedSchemaContextBuilderFactory.build(configFilePath) >> builder
            1 * builder.build() >> context
            1 * ddlWriter.saveCreteScripts(creationScriptPath, context)
            0 * ddlWriter.saveDropScripts(_, context)

        and: "file with checking statements should not be created"
            0 * ddlWriter.saveCheckingStatements(_, context)

        where:
            configFilePath                  |   creationScriptPath
            "C:\\some\\path\\config.yml"    |   "Z:\\create-shared-schema.sql"
            "dir/cofing.xml"                |   "create-schema.sql"
    }

    @Unroll
    def "should create DDL scripts for file #configFilePath and save dropping script in file #droppingScriptPath but do not save creation script when file is not specified"()
    {
        given:
            def defaultSharedSchemaContextBuilderFactoryResolver = Mock(DefaultSharedSchemaContextBuilderFactoryResolver)
            def ddlWriter = Mock(DDLWriter)
            def tested = new DDLScriptsGenerator(defaultSharedSchemaContextBuilderFactoryResolver, ddlWriter)
            IDefaultSharedSchemaContextBuilderFactory defaultSharedSchemaContextBuilderFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
            DefaultSharedSchemaContextBuilder builder = Mock(DefaultSharedSchemaContextBuilder)
            ISharedSchemaContext context = Mock(ISharedSchemaContext)

        when:
            tested.generate(configFilePath, null, droppingScriptPath, null)

        then:
            1 * defaultSharedSchemaContextBuilderFactoryResolver.resolve(configFilePath) >> defaultSharedSchemaContextBuilderFactory
            1 * defaultSharedSchemaContextBuilderFactory.build(configFilePath) >> builder
            1 * builder.build() >> context
            0 * ddlWriter.saveCreteScripts(_, context)
            1 * ddlWriter.saveDropScripts(droppingScriptPath, context)

        and: "file with checking statements should not be created"
            0 * ddlWriter.saveCheckingStatements(_, context)

        where:
            configFilePath                  |   droppingScriptPath
            "C:\\some\\path\\config.yml"    |   ".\\drop-shared-schema.sql"
            "dir/cofing.xml"                |   "drop-schema.sql"
    }
}
