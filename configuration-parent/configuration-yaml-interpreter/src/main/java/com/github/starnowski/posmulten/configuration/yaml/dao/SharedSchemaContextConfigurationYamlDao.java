/**
 *     Posmulten library is an open-source project for the generation
 *     of SQL DDL statements that make it easy for implementation of
 *     Shared Schema Multi-tenancy strategy via the Row Security
 *     Policies in the Postgres database.
 *
 *     Copyright (C) 2020  Szymon Tarnowski
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */
package com.github.starnowski.posmulten.configuration.yaml.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema;
import com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.validation.groups.ValidatorGroupsResolver;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.*;
import javax.validation.groups.Default;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

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

    public SharedSchemaContextConfiguration readFromContent(String content) throws IOException, YamlInvalidSchema {
        SharedSchemaContextConfiguration result = mapper.readValue(content, SharedSchemaContextConfiguration.class);
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
        ValidatorGroupsResolver validatorGroupsResolver = new ValidatorGroupsResolver();
        List<Class> validationGroupsList = validatorGroupsResolver.resolveForSharedSchemaContextConfiguration(configuration, null);
        validationGroupsList.add(Default.class);
        Class[] validationGroups = validationGroupsList.stream().filter(Objects::nonNull).collect(toList()).toArray(new Class[0]);
        Set<ConstraintViolation<SharedSchemaContextConfiguration>> errors = validator.validate(configuration, validationGroups);
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
