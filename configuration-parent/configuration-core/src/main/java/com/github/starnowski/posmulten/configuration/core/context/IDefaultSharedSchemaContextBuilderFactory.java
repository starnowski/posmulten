package com.github.starnowski.posmulten.configuration.core.context;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder;

public interface IDefaultSharedSchemaContextBuilderFactory {

    DefaultSharedSchemaContextBuilder build(String filePath) throws InvalidConfigurationException;
}
