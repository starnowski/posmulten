package com.github.starnowski.posmulten.configuration.yaml.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;

import java.io.File;
import java.io.IOException;

public class SharedSchemaContextConfigurationYamlDao {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public SharedSchemaContextConfiguration read(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), SharedSchemaContextConfiguration.class);
    }

    public void save(SharedSchemaContextConfiguration configuration, String filePath) throws IOException {
        mapper.writeValue(new File(filePath), configuration);
    }
}
