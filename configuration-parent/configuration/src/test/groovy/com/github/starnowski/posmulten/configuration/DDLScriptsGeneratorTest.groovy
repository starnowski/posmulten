package com.github.starnowski.posmulten.configuration

import com.github.starnowski.posmulten.configuration.core.context.DDLWriter
import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import spock.lang.Specification
import spock.lang.Unroll

class DDLScriptsGeneratorTest extends Specification {

    @Unroll
    def "should create DDL scripts for file #configFilePath and save creation script in file #creationScriptPath and dropping script in file #droppingScriptPath"()
    {
        given:
            def defaultSharedSchemaContextBuilderFactoryResolver = Mock(DefaultSharedSchemaContextBuilderFactoryResolver)
            def ddlWriter = Mock(DDLWriter)
            def tested = new DDLScriptsGenerator(defaultSharedSchemaContextBuilderFactoryResolver, ddlWriter)
            IDefaultSharedSchemaContextBuilderFactory defaultSharedSchemaContextBuilderFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
            DefaultSharedSchemaContextBuilder builder = Mock(DefaultSharedSchemaContextBuilder)
            ISharedSchemaContext context = Mock(ISharedSchemaContext)

        when:
            tested.generate(configFilePath, creationScriptPath, droppingScriptPath)

        then:
            1 * defaultSharedSchemaContextBuilderFactoryResolver.resolve(configFilePath) >> defaultSharedSchemaContextBuilderFactory
            1 * defaultSharedSchemaContextBuilderFactory.build(configFilePath) >> builder
            1 * builder.build() >> context
            1 * ddlWriter.saveCreteScripts(creationScriptPath, context)
            1 * ddlWriter.saveDropScripts(droppingScriptPath, context)

        where:
            configFilePath                  |   creationScriptPath              |   droppingScriptPath
            "C:\\some\\path\\config.yml"    |   "Z:\\create-shared-schema.sql"  |   ".\\drop-shared-schema.sql"
            "dir/cofing.xml"                |   "create-schema.sql"             |   "drop-schema.sql"
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
            tested.generate(configFilePath, creationScriptPath, null)

        then:
            1 * defaultSharedSchemaContextBuilderFactoryResolver.resolve(configFilePath) >> defaultSharedSchemaContextBuilderFactory
            1 * defaultSharedSchemaContextBuilderFactory.build(configFilePath) >> builder
            1 * builder.build() >> context
            1 * ddlWriter.saveCreteScripts(creationScriptPath, context)
            0 * ddlWriter.saveDropScripts(_, context)

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
            tested.generate(configFilePath, null, droppingScriptPath)

        then:
            1 * defaultSharedSchemaContextBuilderFactoryResolver.resolve(configFilePath) >> defaultSharedSchemaContextBuilderFactory
            1 * defaultSharedSchemaContextBuilderFactory.build(configFilePath) >> builder
            1 * builder.build() >> context
            0 * ddlWriter.saveCreteScripts(_, context)
            1 * ddlWriter.saveDropScripts(droppingScriptPath, context)

        where:
            configFilePath                  |   droppingScriptPath
            "C:\\some\\path\\config.yml"    |   ".\\drop-shared-schema.sql"
            "dir/cofing.xml"                |   "drop-schema.sql"
    }
}
