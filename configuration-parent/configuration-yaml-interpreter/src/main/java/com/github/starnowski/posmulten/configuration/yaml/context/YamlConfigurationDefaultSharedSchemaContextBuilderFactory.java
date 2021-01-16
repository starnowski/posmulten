package com.github.starnowski.posmulten.configuration.yaml.context;

import com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactory;
import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.dao.SharedSchemaContextConfigurationYamlDao;
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.configuration.yaml.mappers.SharedSchemaContextConfigurationMapper;

import java.io.IOException;

public class YamlConfigurationDefaultSharedSchemaContextBuilderFactory extends AbstractDefaultSharedSchemaContextBuilderFactory {

    private final SharedSchemaContextConfigurationYamlDao dao = new SharedSchemaContextConfigurationYamlDao();
    private final SharedSchemaContextConfigurationMapper mapper = new SharedSchemaContextConfigurationMapper();

    @Override
    protected SharedSchemaContextConfiguration prepareConfigurationBasedOnFile(String filePath) throws YamlInvalidSchema {
        com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration yamlConfiguration = null;
        try {
            yamlConfiguration = dao.read(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mapper.unmap(yamlConfiguration);
    }
}
