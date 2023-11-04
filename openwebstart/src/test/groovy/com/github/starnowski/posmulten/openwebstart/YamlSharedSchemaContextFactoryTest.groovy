package com.github.starnowski.posmulten.openwebstart

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory
import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext
import com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSGranteeDeclarationException
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException
import spock.lang.Subject

class YamlSharedSchemaContextFactoryTest extends spock.lang.Specification {

    @Subject
    YamlSharedSchemaContextFactory yamlSharedSchemaContextFactory

    def setup() {
        yamlSharedSchemaContextFactory = new YamlSharedSchemaContextFactory()
    }

    def "it should build ISharedSchemaContext using default factory and decorator"() {
        given:
        String yaml = """  # Your YAML content here """
        DefaultDecoratorContext decoratorContext = new DefaultDecoratorContext()

        when:
        def result = yamlSharedSchemaContextFactory.build(yaml, decoratorContext)

        then:
        result instanceof ISharedSchemaContext
    }

    def "it should build ISharedSchemaContext using custom factory and decorator"() {
        given:
        String yaml = """  # Your YAML content here """
        DefaultDecoratorContext decoratorContext = new DefaultDecoratorContext()
        def customFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
        def customDecoratorFactory = Mock(SharedSchemaContextDecoratorFactory)

        yamlSharedSchemaContextFactory = new YamlSharedSchemaContextFactory(customFactory, customDecoratorFactory)
        customFactory.buildForContent(_) >> new DefaultSharedSchemaContextBuilder().build()
        customDecoratorFactory.build(_, decoratorContext) >> Mock(ISharedSchemaContext)

        when:
        def result = yamlSharedSchemaContextFactory.build(yaml, decoratorContext)

        then:
        result instanceof ISharedSchemaContext
    }

    def "it should throw InvalidConfigurationException for invalid YAML"() {
        given:
        String invalidYaml = "invalid_yaml"
        DefaultDecoratorContext decoratorContext = new DefaultDecoratorContext()

        when:
        def result = { yamlSharedSchemaContextFactory.build(invalidYaml, decoratorContext) }

        then:
        result() == InvalidConfigurationException
    }

    def "it should throw SharedSchemaContextBuilderException for an invalid builder"() {
        given:
        String yaml = """  # Your YAML content here """
        DefaultDecoratorContext decoratorContext = new DefaultDecoratorContext()
        def customFactory = Mock(IDefaultSharedSchemaContextBuilderFactory)
        def customDecoratorFactory = Mock(SharedSchemaContextDecoratorFactory)

        yamlSharedSchemaContextFactory = new YamlSharedSchemaContextFactory(customFactory, customDecoratorFactory)
        customFactory.buildForContent(_) >> { throw new MissingRLSGranteeDeclarationException("") }

        when:
        def result = { yamlSharedSchemaContextFactory.build(yaml, decoratorContext) }

        then:
        result() == SharedSchemaContextBuilderException
    }
}