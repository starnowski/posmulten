
The module is packaged as the uber jar that ables to generated DDL scripts that create the shared schema strategy based on configuration. 
Currently, the module supports only the YAML file extensions. 
To see how to prepare a configuration file, please check the [configuration-yaml-interpreter](../configuration-yaml-interpreter) module.
The jar file required JAVA version 8 or newer.

### Usage
Executable jar based on configuration generates SQL script files that contain DDL statements that create the shared schema strategy. 
The other one has a DDL statement that drops strategy.

__Properties required to generate scripts__

| Property name |   Required    | Description                                                                                                                                                                                                    |
|---------------|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|posmulten.configuration.config.file.path   |   Yes | File path to configuration file                                                                                                                                                                                |
|posmulten.configuration.create.script.path |   No | File path to generated script that creates the shared schema strategy                                                                                                                                          |
|posmulten.configuration.drop.script.path   |   No | File path to generated script that drops the shared schema strategy                                                                                                                                            |
|posmulten.configuration.validation.statements.path  |   No | File path to script that contains validation queries for the shared schema strategy                                                                                                                            |
|posmulten.configuration.config.context.decorator.replaceCharactersMap  |   No | Pair of template values used in configuration where key is going to be replace with its value during script generation. For example, "{{db_rls_grantee}}=my_user,{{db_schema}}=some_schema"                    |
|posmulten.configuration.config.context.decorator.replaceCharactersMap.separator  |   No | Regex string as a separator for pair of template values passed with property "posmulten.configuration.config.context.decorator.replaceCharactersMap". By default, the separator is "," character. Please be in mind that if any passed characters are special from a regex pattern perspective then such characters should be escaped |

Example:

Generate scripts for the some-conf.yml configuration file. 
The DDL statements that create the shared schema strategy are going to be stored in the create_script.sql file and those ones that drop it are going to be stored in the drop_script.sql file.

```bash
java -Dposmulten.configuration.config.file.path="/path/to/file/some-conf.yml" -Dposmulten.configuration.create.script.path="/some/dir/create_script.sql" -Dposmulten.configuration.drop.script.path="path/for/drop/script/drop_script.sql" -jar "/some/path/configuration-jar-jar-with-dependencies.jar"
```

Generate scripts for the configuration-with-template-values.yaml configuration file.
The DDL statements that create the shared schema strategy are going to be stored in the create_script.sql file and template values like "{{db_rls_grantee}}" and "{{db_schema}}" are going to be replaced with specified values.

```bash
java -Dposmulten.configuration.config.file.path="/path/to/file/configuration-with-template-values.yaml" -Dposmulten.configuration.create.script.path="/some/dir/create_script.sql" -Dposmulten.configuration.config.context.decorator.replaceCharactersMap='{{db_rls_grantee}}=my_user,{{db_schema}}=some_schema' -jar "/some/path/configuration-jar-jar-with-dependencies.jar"
```

Printing the jar file version:

__Property required to print jar version__

| Property name |   Required    |   Description |
|---------------|-----------|---------------|
|posmulten.configuration.config.version.print   |   Yes |   Property that force the jar file to print its version. Property required to have assigned value __true__  |

Example:

```bash
java -Dposmulten.configuration.config.version.print="true" -jar "/some/path/configuration-jar-jar-with-dependencies.jar"
```

Output:

```bash
0.3.0
```