package com.github.starnowski.posmulten.configuration.yaml.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class SharedSchemaContextConfigurationYamlDao {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public SharedSchemaContextConfiguration read(String filePath) throws IOException {
        SharedSchemaContextConfiguration result = mapper.readValue(new File(filePath), SharedSchemaContextConfiguration.class);
//        validateConfigurationObject(result);
        return result;
    }

    public void save(SharedSchemaContextConfiguration configuration, String filePath) throws IOException {
//        validateConfigurationObject(configuration);
        mapper.writeValue(new File(filePath), configuration);
    }

    private void validateConfigurationObject(SharedSchemaContextConfiguration configuration)
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<SharedSchemaContextConfiguration>> errors = validator.validate(configuration);
        if (!errors.isEmpty())
        {
            throw new RuntimeException(prepareErrorMessage(errors));
        }
    }

    private String prepareErrorMessage(Set<ConstraintViolation<SharedSchemaContextConfiguration>> errors)
    {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<SharedSchemaContextConfiguration> error : errors) {
            sb.append(error.getPropertyPath());
            sb.append(" ");
            sb.append(error.getMessage());
        }
        return sb.toString();
    }
}
