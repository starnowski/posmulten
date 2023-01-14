package com.github.starnowski.posmulten.configuration.yaml.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.validation.ElementKind.CONTAINER_ELEMENT;
import static javax.validation.ElementKind.PROPERTY;

public class SharedSchemaContextConfigurationYamlDao {

    private final ObjectMapper mapper;

    public SharedSchemaContextConfigurationYamlDao() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new Jdk8Module());
    }

    public SharedSchemaContextConfiguration read(String filePath) throws IOException, YamlInvalidSchema {
        SharedSchemaContextConfiguration result = mapper.readValue(new File(filePath), SharedSchemaContextConfiguration.class);
        validateConfigurationObject(result);
        return result;
    }

    public void save(SharedSchemaContextConfiguration configuration, String filePath) throws IOException, YamlInvalidSchema {
        validateConfigurationObject(configuration);
        mapper.writeValue(new File(filePath), configuration);
    }

    private void validateConfigurationObject(SharedSchemaContextConfiguration configuration) throws YamlInvalidSchema {
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
                    if (PROPERTY.equals(parentImpl.getKind())) {
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

    private void prepareNodePathBasedOnParentNodeClass(List<String> nodes, Path.Node node, Class<?> parentNodeClass) {
        try {
            //TODO
            if (node.getName() == null) {
                return;
            }
            Field field = parentNodeClass.getDeclaredField(node.getName());
            JsonProperty annotation = field.getAnnotation(JsonProperty.class);
            if (annotation != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(annotation.value() == null ? node.getName() : annotation.value());
                NodeImpl nodeImpl = (NodeImpl) node;
                if (nodeImpl.isIterable() && returnNodeIndex(nodeImpl) != null) {
                    sb.append("[");
                    sb.append(returnNodeIndex(nodeImpl));
                    sb.append("]");
                }
                nodes.add(sb.toString());
            }
        } catch (NoSuchFieldException e) {
            if (CONTAINER_ELEMENT.equals(node.getKind())) {
                if (node.getKey() != null) {
                    NodeImpl nodeImpl = (NodeImpl) node;
                    //nodeImpl.getTypeArgumentIndex() == 0 <- map key, nodeImpl.getTypeArgumentIndex() == 1 <- map value
                    if (nodeImpl.getTypeArgumentIndex() != null && nodeImpl.getTypeArgumentIndex() != 0) {
                        nodes.add(node.getKey().toString());
                        return;
                    }
                }
            }
            nodes.add(node.getName());
        }
    }

    private Integer returnNodeIndex(NodeImpl nodeImpl) {
        NodeImpl tmpNode = NodeImpl.createBeanNode(nodeImpl);
        return tmpNode.getIndex();
    }


}
