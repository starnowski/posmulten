package com.github.starnowski.posmulten.configuration.yaml.context;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;

public class YamlConfigurationDefaultSharedSchemaContextBuilderFactory extends AbstractDefaultSharedSchemaContextBuilderFactory {
    @Override
    protected SharedSchemaContextConfiguration prepareConfigurationBasedOnFile(String filePath) {
        return null;
    }
}
