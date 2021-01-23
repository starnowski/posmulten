TODO

Check the [configuration-yaml-interpreter](../configuration-yaml-interpreter) module to see YAML specification.

The module is packaged as the uber jar that ables to generated DDL scripts that create the shared schema strategy based on configuration. 
Currently, the module supports only the YAML file extensions. 
To see how to prepare a configuration file, please check the [configuration-yaml-interpreter](../configuration-yaml-interpreter) module.

### Usage
Executable jar based on configuration generate two sql script files that one contains DDL statements that creates the shared schema strategy.
The other one has DDL statement that drops strategy.

__Properties required to generate scripts__
| Property name |   Required    |   Description |
|---------------|-----------|---------------|
|posmulten.configuration.config.file.path   |   Yes |   File path to configuration file  |
|posmulten.configuration.create.script.path   |   Yes |   File path to generated script that creates the shared schema strategy |
|posmulten.configuration.drop.script.path   |   Yes |   File path to generated script that drops the shared schema strategy |
