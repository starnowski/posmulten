package com.github.starnowski.posmulten.openwebstart

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory
import com.github.starnowski.posmulten.configuration.yaml.core.exceptions.YamlInvalidSchema
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext
import com.github.starnowski.posmulten.postgresql.core.context.decorator.ISharedSchemaContextDecorator
import com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSGranteeDeclarationException

class YamlSharedSchemaContextFactoryTest extends spock.lang.Specification {

    YamlSharedSchemaContextFactory yamlSharedSchemaContextFactory

    def "it should build ISharedSchemaContext using default factory and decorator"() {
        given:
            String yaml = """  # Your YAML content here """
            DefaultDecoratorContext decoratorContext = new DefaultDecoratorContext()
            def customFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
            def customDecoratorFactory = Mock(SharedSchemaContextDecoratorFactory)
            yamlSharedSchemaContextFactory = new YamlSharedSchemaContextFactory(customFactory, customDecoratorFactory)
            def context1 = Mock(ISharedSchemaContext)
            def builder = Mock(DefaultSharedSchemaContextBuilder)
            def context2 = Mock(ISharedSchemaContextDecorator)
            customFactory.buildForContent(yaml) >> builder
            builder.build() >> context1
            customDecoratorFactory.build(context1, decoratorContext) >> context2

        when:
            def result = yamlSharedSchemaContextFactory.build(yaml, decoratorContext)

        then:
            result == context2
    }

    def "it should throw InvalidConfigurationException for invalid YAML"() {
        given:
            String yaml = """  # Your YAML content here """
            DefaultDecoratorContext decoratorContext = new DefaultDecoratorContext()
            def customFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
            yamlSharedSchemaContextFactory = new YamlSharedSchemaContextFactory(customFactory, null)
            def yamlException = new YamlInvalidSchema(["test"])
            customFactory.buildForContent(yaml) >> {throw yamlException}

        when:
            yamlSharedSchemaContextFactory.build(yaml, null)

        then:
            def ex = thrown(YamlInvalidSchema)
            ex == yamlException
    }

    def "it should throw SharedSchemaContextBuilderException for an invalid builder"() {
        given:
            String yaml = """  # Your YAML content here """
            DefaultDecoratorContext decoratorContext = new DefaultDecoratorContext()
            def customFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
            yamlSharedSchemaContextFactory = new YamlSharedSchemaContextFactory(customFactory, null)
            def builderException = new MissingRLSGranteeDeclarationException("")
            def context1 = Mock(ISharedSchemaContext)
            def builder = Mock(DefaultSharedSchemaContextBuilder)
            def context2 = Mock(ISharedSchemaContextDecorator)
            customFactory.buildForContent(yaml) >> builder
            builder.build() >> {throw builderException}

        when:
            yamlSharedSchemaContextFactory.build(yaml, null)

        then:
            def ex = thrown(MissingRLSGranteeDeclarationException)
            ex == builderException
    }
}