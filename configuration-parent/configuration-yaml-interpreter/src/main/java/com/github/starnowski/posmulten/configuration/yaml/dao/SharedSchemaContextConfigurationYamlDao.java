package com.github.starnowski.posmulten.configuration.yaml.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.validation.ElementKind.BEAN;

public class SharedSchemaContextConfigurationYamlDao {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public SharedSchemaContextConfiguration read(String filePath) throws IOException {
        SharedSchemaContextConfiguration result = mapper.readValue(new File(filePath), SharedSchemaContextConfiguration.class);
        validateConfigurationObject(result);
        return result;
    }

    public void save(SharedSchemaContextConfiguration configuration, String filePath) throws IOException {
        validateConfigurationObject(configuration);
        mapper.writeValue(new File(filePath), configuration);
    }

    private void validateConfigurationObject(SharedSchemaContextConfiguration configuration) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<SharedSchemaContextConfiguration>> errors = validator.validate(configuration);
        if (!errors.isEmpty()) {
            throw new YamlInvalidSchema(prepareErrorsMessages(errors));
        }
    }

    private List<String> prepareErrorsMessages(Set<ConstraintViolation<SharedSchemaContextConfiguration>> errors) {
        return errors.stream().map(error -> prepareErrorMessage(error)).collect(toList());
    }

    private String prepareErrorMessage(ConstraintViolation<SharedSchemaContextConfiguration> error) {
        StringBuilder sb = new StringBuilder();
        sb.append(resolvePropertyPath(error));
        sb.append(" ");
        sb.append(error.getMessage());
        return sb.toString();
    }

    private String resolvePropertyPath(ConstraintViolation<SharedSchemaContextConfiguration> error) {
        StringBuilder sb = new StringBuilder();
        Path path = error.getPropertyPath();
        if (path instanceof PathImpl) {
            List<String> nodes = new ArrayList<>();
            PathImpl pathImpl = (PathImpl) path;
            Iterator<Path.Node> it = pathImpl.iterator();
            Path.Node parent = null;
            for (; it.hasNext(); ) {
                Path.Node node = it.next();
                if (parent == null) {
                    Class<?> keyClass = SharedSchemaContextConfiguration.class;
                    prepareNodePathBasedOnParentNodeClass(nodes, node, keyClass);
                } else {
                    NodeImpl parentImpl = (NodeImpl) parent;
                    if (ElementKind.PROPERTY.equals(parentImpl.getKind()))
                    {
                        Class<?> keyClass = parentImpl.getValue().getClass();
                        prepareNodePathBasedOnParentNodeClass(nodes, node, keyClass);
                    }
                }
                parent = node;
            }
            sb.append(nodes.stream().collect(joining(".")));
        } else {
            sb.append(error.getPropertyPath());
        }
        return sb.toString();
    }

    private void prepareNodePathBasedOnParentNodeClass(List<String> nodes, Path.Node node, Class<?> keyClass) {
        try {
            Field field = keyClass.getDeclaredField(node.getName());
            JsonProperty annotation = field.getAnnotation(JsonProperty.class);
            if (annotation != null) {
                nodes.add(annotation.value() == null ? node.getName() : annotation.value());
            }
        } catch (NoSuchFieldException e) {
            nodes.add(node.getName());
        }
    }


}
