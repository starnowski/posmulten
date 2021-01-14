package com.github.starnowski.posmulten.configuration.core.context;

import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.Files.write;
import static java.util.stream.Collectors.toList;

public class DDLWriter {

    public void saveCreteScripts(String filePath, ISharedSchemaContext sharedSchemaContext) throws IOException {
        List<String> lines = sharedSchemaContext.getSqlDefinitions().stream().map(SQLDefinition::getCreateScript).collect(toList());
        write(Paths.get(new File(filePath).toURI()), lines);
    }

    public void saveDropScripts(String filePath, ISharedSchemaContext sharedSchemaContext) throws IOException {
        //Save DDL statements in reverse order
        LinkedList<SQLDefinition> stack = new LinkedList<>();
        sharedSchemaContext.getSqlDefinitions().forEach(stack::push);
        List<String> lines = stack.stream().map(SQLDefinition::getDropScript).collect(toList());
        write(Paths.get(new File(filePath).toURI()), lines);
    }
}
