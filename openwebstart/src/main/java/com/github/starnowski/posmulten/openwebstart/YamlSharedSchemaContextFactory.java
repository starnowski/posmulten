package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.configuration.yaml.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;

public class YamlSharedSchemaContextFactory {

    public YamlSharedSchemaContextFactory(IDefaultSharedSchemaContextBuilderFactory factory, SharedSchemaContextDecoratorFactory sharedSchemaContextDecoratorFactory) {
        this.factory = factory;
        this.sharedSchemaContextDecoratorFactory = sharedSchemaContextDecoratorFactory;
    }

    public YamlSharedSchemaContextFactory() {
        this(new YamlConfigurationDefaultSharedSchemaContextBuilderFactory(), new SharedSchemaContextDecoratorFactory());
    }

    private final IDefaultSharedSchemaContextBuilderFactory factory;
    private final SharedSchemaContextDecoratorFactory sharedSchemaContextDecoratorFactory;

    public ISharedSchemaContext build(String yaml, DefaultDecoratorContext decoratorContext) throws InvalidConfigurationException, SharedSchemaContextBuilderException {
        DefaultSharedSchemaContextBuilder builder = factory.buildForContent(yaml);
        ISharedSchemaContext defaultContext = builder.build();
        return sharedSchemaContextDecoratorFactory.build(defaultContext, decoratorContext);
    }
}
