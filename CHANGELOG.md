# Posmulten changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

https://keepachangelog.com/en/1.0.0/
https://www.markdownguide.org/basic-syntax/

* [Unreleased](#unreleased)
* [0.9.0](#090---2024-08-18)
* [0.8.1](#081---2024-03-07)
* [0.8.0](#080---2023-11-20)
* [0.7.2](#072---2023-07-03)
* [0.7.1](#071---2023-07-02)
* [0.7.0](#070---2023-06-28)
* [0.6.0](#060---2023-01-14)
* [0.5.6](#056---2022-11-04)
* [0.5.5](#055---2022-06-05)
* [0.5.4](#054---2022-06-05)
* [0.5.3](#053---2022-04-21)
* [0.5.2](#052---2022-04-11)
* [0.5.1](#051---2022-02-14)
* [0.5.0](#050---2022-01-16)
* [0.4.1](#041---2021-08-29)
* [0.4.0](#040---2021-01-30)
* [0.3.1](#031---2021-01-29)
* [0.3.0](#030---2021-01-24)
* [0.2.2](#022---2021-01-10)
* [0.2.1](#021---2020-12-23)
* [0.2.0](#020---2020-11-14)

## [Unreleased]

## [0.9.0] - 2024-08-18

### Added

- Create similar configuration interpreter as configuration-parent/configuration-yaml-interpreter for yaml files which use jakarta package instead of javax [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.context.AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactory type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.context.AbstractYamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.dao.AbstractSharedSchemaContextConfigurationYamlDao type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractConfigurationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractCustomDefinitionEntryMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractForeignKeyConfigurationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractPrimaryKeyDefinitionMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractRLSPolicyMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractSharedSchemaContextConfigurationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractSqlDefinitionsValidationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractTableEntryMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractValidTenantValueConstraintConfigurationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractCustomDefinitionEntry type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractForeignKeyConfiguration type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractPrimaryKeyDefinition type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractRLSPolicy type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSharedSchemaContextConfiguration type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSqlDefinitionsValidation type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractStringWrapperWithNotBlankValue type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractStringWrapperWithNullValue type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractTableEntry type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractValidTenantValueConstraintConfiguration type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactory type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactory type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers.CustomDefinitionEntryMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers.ForeignKeyConfigurationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers.PrimaryKeyDefinitionMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers.RLSPolicyMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers.SharedSchemaContextConfigurationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers.SqlDefinitionsValidationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers.TableEntryMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers.ValidTenantValueConstraintConfigurationMapper type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.CustomDefinitionEntry type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.ForeignKeyConfiguration type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.PrimaryKeyDefinition type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.RLSPolicy type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.SharedSchemaContextConfiguration type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.SqlDefinitionsValidation type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.StringWrapperWithNotBlankValue type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.StringWrapperWithNullValue type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.TableEntry type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.model.ValidTenantValueConstraintConfiguration type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.CustomPositionValidValue type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.CustomPositionValidator type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.EnumNamePattern type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.EnumNamePatternValidator type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.groups.NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.groups.ValidatorGroupsResolver type. [294](https://github.com/starnowski/posmulten/issues/294)
    - Added com.github.starnowski.posmulten.configuration.yaml.jakarta.validation.groups.ValidatorGroupsResolver type. [294](https://github.com/starnowski/posmulten/issues/294)

### Changed

- Create similar configuration interpreter as configuration-parent/configuration-yaml-interpreter for yaml files which use jakarta package instead of javax [294](https://github.com/starnowski/posmulten/issues/294)
    - Moved class com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper to package com.github.starnowski.posmulten.configuration.yaml.core [294](https://github.com/starnowski/posmulten/issues/294)
    - Moved class com.github.starnowski.posmulten.configuration.yaml.exceptions.InvalidConfigurationException to package com.github.starnowski.posmulten.configuration.yaml.core.exceptions [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.dao.SharedSchemaContextConfigurationYamlDao implements com.github.starnowski.posmulten.configuration.yaml.core.dao.AbstractSharedSchemaContextConfigurationYamlDao [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.mappers.CustomDefinitionEntryMapper extends com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractCustomDefinitionEntryMapper [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.mappers.ForeignKeyConfigurationMapper extends com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractForeignKeyConfigurationMapper [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.mappers.PrimaryKeyDefinitionMapper extends com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractPrimaryKeyDefinitionMapper [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.mappers.RLSPolicyMapper extends com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractRLSPolicyMapper [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.mappers.SharedSchemaContextConfigurationMapper extends com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractSharedSchemaContextConfigurationMapper [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.mappers.SqlDefinitionsValidationMapper extends com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractSqlDefinitionsValidationMapper [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.mappers.TableEntryMapper extends com.github.starnowski.posmulten.configuration.yaml.core.mappers.AbstractTableEntryMapper [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry implements com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractCustomDefinitionEntry [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration implements com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractForeignKeyConfiguration [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition implements com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractPrimaryKeyDefinition [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy implements com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractRLSPolicy [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration implements com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSharedSchemaContextConfiguration [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation implements com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSqlDefinitionsValidation [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue implements com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractStringWrapperWithNotBlankValue [294](https://github.com/starnowski/posmulten/issues/294)
    - Class com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration implements com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractValidTenantValueConstraintConfiguration [294](https://github.com/starnowski/posmulten/issues/294)

## [0.8.1] - 2024-03-07

### Added

- Add functionality that create foreign key instead of constraint that check if foreign key belongs to tenant [301](https://github.com/starnowski/posmulten/issues/301)
    - Added property createForeignKeyConstraintWithTenantColumn in com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition.NameForFunctionThatChecksIfRecordExistsInTableNotBlank type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added property createForeignKeyConstraintWithTenantColumn in com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added getter and setter method for property createForeignKeyConstraintWithTenantColumn in com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.configuration.yaml.validation.groups.NameForFunctionThatChecksIfRecordExistsInTableNotBlankGroupResolver type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.configuration.yaml.validation.groups.ValidatorGroupResolver type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.configuration.yaml.validation.groups.ValidatorGroupsResolver type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.postgresql.core.DefaultForeignKeyConstraintStatementParameters type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.postgresql.core.ForeignKeyConstraintStatementProducer type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.postgresql.core.IForeignKeyConstraintStatementParameters type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setIgnoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant(Boolean) method. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setCreateForeignKeyConstraintWithTenantColumn(Boolean) method. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added property createForeignKeyConstraintWithTenantColumn in com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added property ignoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant in com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added getter and setter method for property createForeignKeyConstraintWithTenantColumn in com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added getter and setter method for property ignoreCreationOfConstraintThatChecksIfRecordBelongsToCurrentTenant in com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest type. [301](https://github.com/starnowski/posmulten/issues/301)
    - Added com.github.starnowski.posmulten.postgresql.core.context.enrichers.ForeignKeyConstraintSQLDefinitionsEnricher type. [301](https://github.com/starnowski/posmulten/issues/301)
    
## [0.8.0] - 2023-11-20

### Added

- Create skeleton project for Swing application [295](https://github.com/starnowski/posmulten/issues/295)
    - Added method com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactory#buildForContent(String) [295](https://github.com/starnowski/posmulten/issues/295)
    - Added method com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory#buildForContent(String) [295](https://github.com/starnowski/posmulten/issues/295)
    - Added method com.github.starnowski.posmulten.configuration.yaml.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactory#prepareConfigurationBasedOnContent(String) [295](https://github.com/starnowski/posmulten/issues/295)
    - Added method com.github.starnowski.posmulten.configuration.yaml.dao.SharedSchemaContextConfigurationYamlDao#readFromContent(String) [295](https://github.com/starnowski/posmulten/issues/295)
    - Added type com.github.starnowski.posmulten.openwebstart.ParametersPanel [295](https://github.com/starnowski/posmulten/issues/295)
    - Added type com.github.starnowski.posmulten.openwebstart.PosmultenApp [295](https://github.com/starnowski/posmulten/issues/295)
    - Added type com.github.starnowski.posmulten.openwebstart.ScriptPanel [295](https://github.com/starnowski/posmulten/issues/295)
    - Added type com.github.starnowski.posmulten.openwebstart.SharedSchemaContextComparableResultsPanel [295](https://github.com/starnowski/posmulten/issues/295)
    - Added type com.github.starnowski.posmulten.openwebstart.TextAreaExceptionEnricher [295](https://github.com/starnowski/posmulten/issues/295)
    - Added type com.github.starnowski.posmulten.openwebstart.YamlSharedSchemaContextFactory [295](https://github.com/starnowski/posmulten/issues/295)
    - Added type com.github.starnowski.posmulten.postgresql.core.context.comparable.DefaultSharedSchemaContextComparator [295](https://github.com/starnowski/posmulten/issues/295)
    - Added type com.github.starnowski.posmulten.postgresql.core.context.comparable.SharedSchemaContextComparator [295](https://github.com/starnowski/posmulten/issues/295)

## [0.7.2] - 2023-07-03

### Changed

- Removed condition table_catalog = 'postgresql_core' from check statements [291](https://github.com/starnowski/posmulten/issues/291)
    - Removed invalid condition from SQL query that checks if changes applied by com.github.starnowski.posmulten.postgresql.core.SetNotNullStatementProducer component were added. [291](https://github.com/starnowski/posmulten/issues/291)
    - Removed invalid condition from SQL query that checks if changes applied by com.github.starnowski.posmulten.postgresql.core.SetDefaultStatementProducer component were added. [291](https://github.com/starnowski/posmulten/issues/291)
    - Removed invalid condition from SQL query that checks if changes applied by com.github.starnowski.posmulten.postgresql.core.CreateColumnStatementProducer component were added. [291](https://github.com/starnowski/posmulten/issues/291)

## [0.7.1] - 2023-07-02

### Added

- Added enhancement to DatabaseOperationExecutor and IDatabaseOperationsProcessor types [288](https://github.com/starnowski/posmulten/issues/288)
    - Added method com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationExecutor#execute(Connection, List<SQLDefinition> sqlDefinitions, DatabaseOperationType). [288](https://github.com/starnowski/posmulten/issues/288)
    - Added method com.github.starnowski.posmulten.postgresql.core.db.operations.CreateOperationsProcessor#run(Connection, List<SQLDefinition> sqlDefinitions). [288](https://github.com/starnowski/posmulten/issues/288)
    - Added method com.github.starnowski.posmulten.postgresql.core.db.operations.DatabaseOperationsLoggerProcessor#run(Connection, List<SQLDefinition> sqlDefinitions). [288](https://github.com/starnowski/posmulten/issues/288)
    - Added method com.github.starnowski.posmulten.postgresql.core.db.operations.DropOperationsProcessor#run(Connection, List<SQLDefinition> sqlDefinitions). [288](https://github.com/starnowski/posmulten/issues/288)
    - Added method com.github.starnowski.posmulten.postgresql.core.db.operations.IDatabaseOperationsProcessor#run(Connection, List<SQLDefinition> sqlDefinitions). [288](https://github.com/starnowski/posmulten/issues/288)
    - Added method com.github.starnowski.posmulten.postgresql.core.db.operations.ValidateOperationsProcessor#run(Connection, List<SQLDefinition> sqlDefinitions). [288](https://github.com/starnowski/posmulten/issues/288)

## [0.7.0] - 2023-06-28

### Added

- Added decorator for sqlDefinitions that replace template value like {{schema}} to any passed value in all definitions [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.configuration.jar.Constants type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.configuration.jar.DDLScriptsGeneratorRunner type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.configuration.jar.SystemPropertiesDefaultDecoratorContextSupplier type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.configuration.DDLScriptsGenerator type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.configuration.SystemPropertyReader type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.AbstractSharedSchemaContextDecorator type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.BasicSharedSchemaContextDecorator type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.BasicSharedSchemaContextDecoratorContext type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.BasicSharedSchemaContextDecoratorFactory type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecorator type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.IDecorator type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.ISharedSchemaContextDecorator type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.ISharedSchemaContextDecoratorFactory type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.context.decorator.SharedSchemaContextDecoratorFactory type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationExecutor type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.DatabaseOperationType type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.operations.CreateOperationsProcessor type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.operations.DatabaseOperationsLoggerProcessor type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.operations.DropOperationsProcessor type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.operations.IDatabaseOperationsProcessor type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.operations.ValidateOperationsProcessor type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.operations.ValidationDatabaseOperationsException type. [283](https://github.com/starnowski/posmulten/issues/283)
    - Added com.github.starnowski.posmulten.postgresql.core.db.operations.util.SQLUtil type. [283](https://github.com/starnowski/posmulten/issues/283)

## [0.6.0] - 2023-01-14

### Changed

- Changed method signature com.github.starnowski.posmulten.configuration.core.ForeignKeyConfigurationEnricher#enrich(com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder, String, com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration) to
    com.github.starnowski.posmulten.configuration.core.ForeignKeyConfigurationEnriche#enrichcom.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder, String, java.util.Optional<String>, com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration)
    [245](https://github.com/starnowski/posmulten/issues/245)

### Added

- Added feature that allows to specify custom sql definitions in configuration file [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.core.CustomDefinitionEntriesEnricher type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.core.CustomDefinitionEntryEnricher type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added field customDefinitions of type java.util.List<com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry> to com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.yaml.mappers.CustomDefinitionEntryMapper type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added field customSQLDefinitions of type java.util.List<com.github.starnowski.posmulten.configuration.yaml.CustomDefinitionEntry> to com.github.starnowski.posmulten.configuration.yaml.SharedSchemaContextConfiguration type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.yaml.validation.CustomPositionValidValue type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.yaml.validation.CustomPositionValidator type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.yaml.validation.EnumNamePattern type. [256](https://github.com/starnowski/posmulten/issues/256)
    - Added com.github.starnowski.posmulten.configuration.yaml.validation.EnumNamePatternValidator type. [256](https://github.com/starnowski/posmulten/issues/256)
    
- Added feature that allows to specify schema now in configuration file [245](https://github.com/starnowski/posmulten/issues/245)
    - Added field tableSchema of type java.util.Optional<String> to com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration type. [245](https://github.com/starnowski/posmulten/issues/245)
    - Added field schema of type java.util.Optional<String> to com.github.starnowski.posmulten.configuration.core.model.TableEntry type. [245](https://github.com/starnowski/posmulten/issues/245)
    - Added field tableSchema of type java.util.Optional<String> to com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration type. [245](https://github.com/starnowski/posmulten/issues/245)
    - Added field schema of type java.util.Optional<String> to com.github.starnowski.posmulten.configuration.yaml.model.TableEntry type. [245](https://github.com/starnowski/posmulten/issues/245)
    - Added com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNullValue type. [245](https://github.com/starnowski/posmulten/issues/245)
    - Added ValidationMessages.properties file for default translation of error messages related to configuration-yaml-interpreter module. [245](https://github.com/starnowski/posmulten/issues/245)

## [0.5.6] - 2022-11-04

### Changed

- Can be compiled only with "-DskipTests=true" option when there are different locale setups than "en" [270](https://github.com/starnowski/posmulten/issues/270)
  - Updated project descriptor for com.github.starnowski.posmulten.configuration:configuration-yaml-interpreter module, setting the "en" as locale setup for tests execution


## [0.5.5] - 2022-06-05

### Changed
    
- Updated postgres dependency to 4.3.3 [263](https://github.com/starnowski/posmulten/issues/263)

## [0.5.4] - 2022-06-05

### Added

- Added two enrichers (one at the beginning and on at the end of list) that will add custom SQL statements
  - Added type com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPair
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added type com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairDefaultPosition
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added type com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider 
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added enricher of com.github.starnowski.posmulten.postgresql.core.context.enrichers.CustomSQLDefinitionsAtBeginningEnricher type as first item of com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#enrichers list
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added enricher of com.github.starnowski.posmulten.postgresql.core.context.enrichers.CustomSQLDefinitionsAtEndEnricher type as last item of com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#enrichers list
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added constant com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#DEFAULT_CUSTOM_SQL_STATEMENT
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#addCustomSQLDefinition(com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider, com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition)
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#addCustomSQLDefinition(com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider, String)
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#addCustomSQLDefinition(com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider, String, String)
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#addCustomSQLDefinition(com.github.starnowski.posmulten.postgresql.core.context.CustomSQLDefinitionPairPositionProvider, String, String, List)
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added property com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#customSQLDefinitionPairs
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#getCustomSQLDefinitionPairs()
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added type com.github.starnowski.posmulten.postgresql.core.context.enrichers.AbstractCustomSQLDefinitionsEnricher
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added type com.github.starnowski.posmulten.postgresql.core.context.enrichers.CustomSQLDefinitionsAtBeginningEnricher
    [252](https://github.com/starnowski/posmulten/issues/252)
  - Added type com.github.starnowski.posmulten.postgresql.core.context.enrichers.CustomSQLDefinitionsAtEndEnricher
    [252](https://github.com/starnowski/posmulten/issues/252)

## [0.5.3] - 2022-04-21

### Added

- Added methods to schema builder that pass table key (schema and table name) similar to methods that pass only the table name
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createRLSPolicyForTable(com.github.starnowski.posmulten.postgresql.core.context.TableKey tableKey, Map<String, String> primaryKeyColumnsList, String tenantColumnName, String rlsPolicyName)
    [239](https://github.com/starnowski/posmulten/issues/239)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createTenantColumnForTable(com.github.starnowski.posmulten.postgresql.core.context.TableKey tableKey)
    [239](https://github.com/starnowski/posmulten/issues/239)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createSameTenantConstraintForForeignKey(com.github.starnowski.posmulten.postgresql.core.context.TableKey mainTableKey, com.github.starnowski.posmulten.postgresql.core.context.TableKey foreignKeyTableKey, Map<String, String> foreignKeyPrimaryKeyColumnsMappings, String constraintName)
    [239](https://github.com/starnowski/posmulten/issues/239)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setNameForFunctionThatChecksIfRecordExistsInTable(com.github.starnowski.posmulten.postgresql.core.context.TableKey recordTableKey, String functionName)
    [239](https://github.com/starnowski/posmulten/issues/239)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#registerCustomValidTenantValueConstraintNameForTable(com.github.starnowski.posmulten.postgresql.core.context.TableKey tableKey, String constraintName)
    [239](https://github.com/starnowski/posmulten/issues/239)
  - Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#skipAddingOfTenantColumnDefaultValueForTable(com.github.starnowski.posmulten.postgresql.core.context.TableKey tableKey)
    [239](https://github.com/starnowski/posmulten/issues/239)

### Changed

-   Passing schema name assigned to table key, not default schema in com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher#enrich(com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext context, com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest request) method
    [239](https://github.com/starnowski/posmulten/issues/239)      
  
## [0.5.2] - 2022-04-11

### Added

-   Added method that resolves the name of tenant column based on table key (schema name, table name)
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#resolveTenantColumnByTableKey(com.github.starnowski.posmulten.postgresql.core.context.TableKey tableKey)
        [214](https://github.com/starnowski/posmulten/issues/214)  

### Changed

-   Updated postgresql test dependency version from 42.2.25 to 42.3.3

## [0.5.1] - 2022-02-14

### Changed

-   Updated postgresql test dependency version from 42.2.5 to 42.2.25

## [0.5.0] - 2022-01-16

### Added
-   Added to ISharedSchemaContext interface method which returns Tenant Column type
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext#setCurrentTenantIdPropertyType(String currentTenantIdPropertyType)
        [218](https://github.com/starnowski/posmulten/issues/218)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext#getCurrentTenantIdPropertyType()
        [218](https://github.com/starnowski/posmulten/issues/218)
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.enrichers.CurrentTenantIdPropertyTypeEnricher
        [218](https://github.com/starnowski/posmulten/issues/218)

-   Printing the content of configuration-yaml-interpreter module README.md file converted to ANSI
    -   Printing the content of configuration-yaml-interpreter module README.md file when setting "posmulten.configuration.config.yaml.syntax.guide.print"
        system property in com.github.starnowski.posmulten.configuration.jar.DDLScriptsGeneratorRunner#main(String[] args) method
        [216](https://github.com/starnowski/posmulten/issues/216)  

-   Added validator component that checks if the passed identifier has the correct name.
    -   Added type com.github.starnowski.posmulten.configuration.core.SqlDefinitionsValidationEnricher
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added property sqlDefinitionsValidation of type com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation to com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added type com.github.starnowski.posmulten.configuration.core.model.SqlDefinitionsValidation
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added usage of com.github.starnowski.posmulten.configuration.yaml.mappers.SqlDefinitionsValidationMapper in methods 
        com.github.starnowski.posmulten.configuration.yaml.mappers.SharedSchemaContextConfigurationMapper#map(com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration input)
        com.github.starnowski.posmulten.configuration.yaml.mappers.SharedSchemaContextConfigurationMapper#unmap(SharedSchemaContextConfiguration output)
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added type com.github.starnowski.posmulten.configuration.yaml.mappers.SqlDefinitionsValidationMapper
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added property sqlDefinitionsValidation of type com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation to com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added type com.github.starnowski.posmulten.configuration.yaml.model.SqlDefinitionsValidation
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added property disableDefaultSqlDefinitionsValidators of type boolean to com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder and setter and getter methods
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setIdentifierMaxLength(Integer identifierMaxLength)
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setIdentifierMinLength(Integer identifierMinLength)
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#getSqlDefinitionsValidatorsCopy()
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setSqlDefinitionsValidators(List<ISQLDefinitionsValidator> sqlDefinitionsValidators)
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added interface com.github.starnowski.posmulten.postgresql.core.context.IIdentifierValidator
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.IdentifierLengthValidator
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added property identifierMaxLength of type java.lang.Integer to com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest and setter and getter methods
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added property identifierMinLength of type java.lang.Integer to com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest and setter and getter methods
        [137](https://github.com/starnowski/posmulten/issues/137)  
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidIdentifierException
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.exceptions.InvalidSharedSchemaContextRequestException
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.validators.FunctionDefinitionValidator
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added interface com.github.starnowski.posmulten.postgresql.core.context.validators.ISQLDefinitionsValidator
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added interface com.github.starnowski.posmulten.postgresql.core.context.validators.factories.IIdentifierValidatorFactory
        [137](https://github.com/starnowski/posmulten/issues/137) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.validators.factories.IdentifierLengthValidatorFactory
        [137](https://github.com/starnowski/posmulten/issues/137) 

-   Added method to the SQLDefinition interface that returns statement which checks if SQL definition was applied correctly.
    -   Added method com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition#getCheckingStatements()
        [65](https://github.com/starnowski/posmulten/issues/65)   
    -   Added method com.github.starnowski.posmulten.configuration.core.context.DDLWriter#saveCheckingStatements(String filePath, ISharedSchemaContext sharedSchemaContext)
        [65](https://github.com/starnowski/posmulten/issues/65)  
    -   Checking system property "posmulten.configuration.validation.statements.path" in com.github.starnowski.posmulten.configuration.jar.DDLWriter#main(String[] args).
        Property value points to the output file path where SQL statements that validate schema are going to be stored.
        [65](https://github.com/starnowski/posmulten/issues/65)   
        
### Changed

-  Added parameter of type com.github.starnowski.posmulten.configuration.core.SqlDefinitionsValidationEnricher to constructor #DefaultSharedSchemaContextBuilderConfigurationEnricher(TablesEntriesEnricher tablesEntriesEnricher, ValidTenantValueConstraintConfigurationEnricher validTenantValueConstraintConfigurationEnricher)
   [137](https://github.com/starnowski/posmulten/issues/137)  

-  Added parameter of type string to method com.github.starnowski.posmulten.configuration.DDLScriptsGenerator#generate(String configurationFilePath, String createScripsFilePath, String dropScripsFilePath)
   Parameter value points to the output file path where SQL statements that validate schema are going to be stored. [65](https://github.com/starnowski/posmulten/issues/65)  

## [0.4.1] - 2021-08-29
### Fixed

- Updated hibernate-validator library from 6.0.18.Final to 6.0.20.Final in configuration-parent/configuration-yaml-interpreter

## [0.4.0] - 2021-01-30
### Fixed

-   Change statement generated by the com.github.starnowski.posmulten.postgresql.core.rls.IsRecordBelongsToCurrentTenantConstraintProducer. [189](https://github.com/starnowski/posmulten/issues/189)
    
    -   Changed the AND operator to OR operator.  
        [189](https://github.com/starnowski/posmulten/issues/189)


## [0.3.1] - 2021-01-29
### Fixed
-   Adding the ';' character at the end of statements that drops/revert changes related to the shared schema strategy. [186](https://github.com/starnowski/posmulten/issues/186) 
    
    -   Adding the ';' character at the end of statements that is generated by method com.github.starnowski.posmulten.postgresql.core.common.function.AbstractFunctionFactory#returnDropScript(P parameters)
        [186](https://github.com/starnowski/posmulten/issues/186)    
    -   Adding the ';' character at the end of statements that is generated by method com.github.starnowski.posmulten.postgresql.core.rls.RLSPolicyProducer#prepareDropScript(RLSPolicyProducerParameters)
        [186](https://github.com/starnowski/posmulten/issues/186)   
        
-   Fixed logging level in file debug-logging.properties in the com.github.starnowski.posmulten.configuration.configuration-jar module.[187](https://github.com/starnowski/posmulten/issues/187) 

## [0.3.0] - 2021-01-24
### Added

-   Create the configuration-core module. [156](https://github.com/starnowski/posmulten/issues/156)

    -   Added type com.github.starnowski.posmulten.configuration.core.DefaultSharedSchemaContextBuilderConfigurationEnricher
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.DefaultSharedSchemaContextBuilderConfigurationInitializingBean
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.DefaultSharedSchemaContextBuilderFactory
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.ForeignKeyConfigurationEnricher
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.ForeignKeyConfigurationsEnricher
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.ITableEntryEnricher
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.RLSPolicyConfigurationEnricher
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.TablesEntriesEnricher
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.ValidTenantValueConstraintConfigurationEnricher
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.model.ForeignKeyConfiguration
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.model.RLSPolicy
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.model.TableEntry
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.mappers.ForeignKeyConfigurationMapper
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.mappers.RLSPolicyMapper
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.mappers.SharedSchemaContextConfigurationMapper
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.mappers.TableEntryMapper
        [156](https://github.com/starnowski/posmulten/issues/156)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.mappers.ValidTenantValueConstraintConfigurationMapper
        [156](https://github.com/starnowski/posmulten/issues/156)
        
-   Created the configuration-jar module. [158](https://github.com/starnowski/posmulten/issues/158)

    -   Added type com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactory
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.core.context.AbstractDefaultSharedSchemaContextBuilderFactorySupplier
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.core.context.DDLWriter
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactory
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.core.context.IDefaultSharedSchemaContextBuilderFactorySupplier
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.jar.DDLScriptsGeneratorRunner
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added the silent-logging.properties file
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactory
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.context.YamlConfigurationDefaultSharedSchemaContextBuilderFactorySupplier
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.DDLScriptsGenerator
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.DefaultSharedSchemaContextBuilderFactoryResolver
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.DefaultSharedSchemaContextBuilderFactoryResolverContext
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.DefaultSharedSchemaContextBuilderFactorySupplierClasspathSearcher
        [158](https://github.com/starnowski/posmulten/issues/158)
    -   Added type com.github.starnowski.posmulten.configuration.FileExtensionExtractor
        [158](https://github.com/starnowski/posmulten/issues/158)
        
-   Created the configuration module. [157](https://github.com/starnowski/posmulten/issues/157)

    -   Added type com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException
        [157](https://github.com/starnowski/posmulten/issues/157)
    -   Added type com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition
        [157](https://github.com/starnowski/posmulten/issues/157)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.exceptions.YamlInvalidSchema
        [157](https://github.com/starnowski/posmulten/issues/157)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.mappers.PrimaryKeyDefinitionMapper
        [157](https://github.com/starnowski/posmulten/issues/157)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.model.PrimaryKeyDefinition
        [157](https://github.com/starnowski/posmulten/issues/157)
        
-   Created the configuration-yaml-interpreter module. [155](https://github.com/starnowski/posmulten/issues/155)

    -   Added type com.github.starnowski.posmulten.configuration.yaml.dao.SharedSchemaContextConfigurationYamlDao
        [155](https://github.com/starnowski/posmulten/issues/155)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration
        [155](https://github.com/starnowski/posmulten/issues/155)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy
        [155](https://github.com/starnowski/posmulten/issues/155)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration
        [155](https://github.com/starnowski/posmulten/issues/155)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.model.TableEntry
        [155](https://github.com/starnowski/posmulten/issues/155)
    -   Added type com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration
        [155](https://github.com/starnowski/posmulten/issues/155)
        
-   Added yaml validation to configuration-yaml-interpreter module. [168](https://github.com/starnowski/posmulten/issues/168)

    -   Added type com.github.starnowski.posmulten.configuration.yaml.model.StringWrapperWithNotBlankValue
        [168](https://github.com/starnowski/posmulten/issues/168)
        
-   Added logging of the SharedSchemaContextBuilderException exception. [182](https://github.com/starnowski/posmulten/issues/182)

    -   Added type com.github.starnowski.posmulten.configuration.NoDefaultSharedSchemaContextBuilderFactorySupplierException
        [182](https://github.com/starnowski/posmulten/issues/182)
        
        
-   Add default constructor to class com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder
    The constructor should invoke a parameterized constructor with the value null. [135](https://github.com/starnowski/posmulten/issues/135) 
    
### Changed

-   Removed unnecessary parameter (3rd) from method #produce(TableKey tableKey, ITableColumns tableColumns, String defaultTenantColumnValue, String defaultTenantColumn, String defaultTenantColumnType)
    [136](https://github.com/starnowski/posmulten/issues/136) 
        
## [0.2.2] - 2021-01-10
### Fixed

-   Using default tenant column name in com.github.starnowski.posmulten.postgresql.core.context.enrichers.DefaultValueForTenantColumnEnricher#enrich(ISharedSchemaContext, SharedSchemaContextRequest) method when tenant column name is not defined for table.
    [174](https://github.com/starnowski/posmulten/issues/174)

## [0.2.1] - 2020-12-23
### Added

-   Added constant __DEFAULT_TENANT_ID_COLUMN__ in type com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest
    [145](https://github.com/starnowski/posmulten/issues/145)    

### Fixed

-   Add ability to pass null map in constructor com.github.starnowski.posmulten.postgresql.core.context.DefaultTableColumns#DefaultTableColumns(String, Map<String, String>)
    [138](https://github.com/starnowski/posmulten/issues/138)
-   Issue related to passing null tenant column name in method DefaultSharedSchemaContextBuilder#createRLSPolicyForTable(String table, Map<String, String> primaryKeyColumnsList, String tenantColumnName, String rlsPolicyName)
    [145](https://github.com/starnowski/posmulten/issues/145)
    
### Changed

-   Changed method parameters, from com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinitionProducer#produce(TableKey, ITableColumns, IGetCurrentTenantIdFunctionInvocationFactory, String, String) to com.github.starnowski.posmulten.postgresql.core.rls.function.IsRecordBelongsToCurrentTenantFunctionDefinitionProducer#produce(TableKey, String, Map<String, String>, IGetCurrentTenantIdFunctionInvocationFactory, String, String)
    [145](https://github.com/starnowski/posmulten/issues/145)
    
## [0.2.0] - 2020-11-14
### Added

-   Interface similar to the "com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionInvocationFactory" 
    that is going to return sql invocation that could be used with PreparedStatement mechanism [105](https://github.com/starnowski/posmulten/issues/105)
    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext#setISetCurrentTenantIdFunctionPreparedStatementInvocationFactory(ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory)
        [105](https://github.com/starnowski/posmulten/issues/105)    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext#getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory()
        [105](https://github.com/starnowski/posmulten/issues/105)    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext#getISetCurrentTenantIdFunctionPreparedStatementInvocationFactory()
        [105](https://github.com/starnowski/posmulten/issues/105)   
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext#setISetCurrentTenantIdFunctionPreparedStatementInvocationFactory(ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory)
        [105](https://github.com/starnowski/posmulten/issues/105)     
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.function.ISetCurrentTenantIdFunctionPreparedStatementInvocationFactory
        [105](https://github.com/starnowski/posmulten/issues/105)    
    -   Added method com.github.starnowski.posmulten.postgresql.core.rls.function.SetCurrentTenantIdFunctionDefinition#returnPreparedStatementThatSetCurrentTenant()
        [105](https://github.com/starnowski/posmulten/issues/105)  

-   Components that generate constraint on tenant column that checks if tenant value is valid and generate function 
    that checks if passed tenant value is not one of values from blacklist [106](https://github.com/starnowski/posmulten/issues/106)

    -   Added type com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionArgumentValueToStringMapper
        [106](https://github.com/starnowski/posmulten/issues/106)
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createValidTenantValueConstraint(List<String>, String, String)
        [106](https://github.com/starnowski/posmulten/issues/106)      
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#registerCustomValidTenantValueConstraintNameForTable(String, String)
        [106](https://github.com/starnowski/posmulten/issues/106)              
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(boolean)
        [106](https://github.com/starnowski/posmulten/issues/106)      
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#skipAddingOfTenantColumnDefaultValueForTable(String)
        [106](https://github.com/starnowski/posmulten/issues/106)     
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext#getIIsTenantValidFunctionInvocationFactory()
        [106](https://github.com/starnowski/posmulten/issues/106)    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext#setIIsTenantValidFunctionInvocationFactory(IIsTenantValidFunctionInvocationFactory)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext#getIIsTenantValidFunctionInvocationFactory
        [106](https://github.com/starnowski/posmulten/issues/106)         
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContext#setIIsTenantValidFunctionInvocationFactory(IIsTenantValidFunctionInvocationFactory)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#getTenantValuesBlacklist()
        [106](https://github.com/starnowski/posmulten/issues/106)    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#setTenantValuesBlacklist(List<String>)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#getIsTenantValidFunctionName()
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#setIsTenantValidFunctionName(String)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#getIsTenantValidConstraintName()
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#setIsTenantValidConstraintName(String)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#isConstraintForValidTenantValueShouldBeAdded()
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#setConstraintForValidTenantValueShouldBeAdded(boolean)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#getTenantValidConstraintCustomNamePerTables()
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(boolean)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#isCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables()
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.SharedSchemaContextRequest#getTablesThatAddingOfTenantColumnDefaultValueShouldBeSkipped()
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.enrichers.DefaultValueForTenantColumnEnricher
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added constructor com.github.starnowski.posmulten.postgresql.core.context.enrichers.GetCurrentTenantIdFunctionDefinitionEnricher#GetCurrentTenantIdFunctionDefinitionEnricher(GetCurrentTenantIdFunctionProducer)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added constructor com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher#IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantIdentifierValidConstraintEnricher
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsTenantValidFunctionInvocationFactoryEnricher
        [106](https://github.com/starnowski/posmulten/issues/106)
    -   Added constructor com.github.starnowski.posmulten.postgresql.core.context.enrichers.SetCurrentTenantIdFunctionDefinitionEnricher#SetCurrentTenantIdFunctionDefinitionEnricher(SetCurrentTenantIdFunctionProducer)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added constructor com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSPolicyEnricher#TableRLSPolicyEnricher(TableRLSPolicySQLDefinitionsProducer)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added constructor com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSSettingsSQLDefinitionsEnricher#TableRLSSettingsSQLDefinitionsEnricher(TableRLSSettingsSQLDefinitionsProducer)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added constructor com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantColumnSQLDefinitionsEnricher#TenantColumnSQLDefinitionsEnricher(SingleTenantColumnSQLDefinitionsProducer)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added constructor com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantHasAuthoritiesFunctionDefinitionEnricher#TenantHasAuthoritiesFunctionDefinitionEnricher(EqualsCurrentTenantIdentifierFunctionProducer, TenantHasAuthoritiesFunctionProducer)
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTableThatRequiredTenantColumnCreationException
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.exceptions.MissingRLSPolicyDeclarationForTablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedException
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.validators.CreateTenantColumnTableMappingSharedSchemaContextRequestValidator
        [106](https://github.com/starnowski/posmulten/issues/106)  
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.validators.TablesThatAddingOfTenantColumnDefaultValueShouldBeSkippedSharedSchemaContextRequestValidator
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.AbstractConstraintProducer
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.DefaultIsTenantIdentifierValidConstraintProducerParameters
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.IConstraintProducerParameters
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.IIsTenantIdentifierValidConstraintProducerParameters
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.IsTenantIdentifierValidConstraintProducer
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidBasedOnConstantValuesFunctionProducerParameters
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.function.IIsTenantValidFunctionInvocationFactory
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionDefinition
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.functionIsTenantValidBasedOnConstantValuesFunctionProducer
        [106](https://github.com/starnowski/posmulten/issues/106) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.rls.function.IsTenantValidBasedOnConstantValuesFunctionProducerParameters
        [106](https://github.com/starnowski/posmulten/issues/106) 
        
-   **Code Refactor** Changed the "Abstract" prefix for all interfaces to "I" prefix.
    -   Added constructor com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher#IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
        [108](https://github.com/starnowski/posmulten/issues/108)  
        
-   Throwing an exception in IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher class when the mapping of 
    foreign keys and primary keys are not correct with the RLS policy declaration for the table. 
    [113](https://github.com/starnowski/posmulten/issues/113)
    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#getValidatorsCopy() 
        [113](https://github.com/starnowski/posmulten/issues/113)    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setValidators(List<ISharedSchemaContextRequestValidator>) 
        [113](https://github.com/starnowski/posmulten/issues/113)
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.exceptions.IncorrectForeignKeysMappingException
        [113](https://github.com/starnowski/posmulten/issues/113)
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.validators.ForeignKeysMappingSharedSchemaContextRequestValidator
        [113](https://github.com/starnowski/posmulten/issues/113) 
    -   Moved type ISharedSchemaContextEnricher to com.github.starnowski.posmulten.postgresql.core.context.enrichers package
        [113](https://github.com/starnowski/posmulten/issues/113) 
    -   Added type com.github.starnowski.posmulten.postgresql.core.context.validators.ISharedSchemaContextRequestValidator
        [113](https://github.com/starnowski/posmulten/issues/113) 

### Changed

-   Renamed method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createRLSPolicyForTable(String, Map<String, String>, String, String)
    from createRLSPolicyForColumn to createRLSPolicyForTable [113](https://github.com/starnowski/posmulten/issues/113)
    
### Removed

-   Removed method com.github.starnowski.posmulten.postgresql.core.context.SingleTenantColumnSQLDefinitionsProducer#setSetDefaultStatementProducer(SetDefaultStatementProducer)
    [106](https://github.com/starnowski/posmulten/issues/106)  
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.GetCurrentTenantIdFunctionDefinitionEnricher#setGetCurrentTenantIdFunctionProducer(GetCurrentTenantIdFunctionProducer)
    [106](https://github.com/starnowski/posmulten/issues/106)  
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantFunctionDefinitionsEnricher#setIsRecordBelongsToCurrentTenantFunctionDefinitionProducer(IsRecordBelongsToCurrentTenantFunctionDefinitionProducer)
    [106](https://github.com/starnowski/posmulten/issues/106)  
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.SetCurrentTenantIdFunctionDefinitionEnricher#setSetCurrentTenantIdFunctionProducer(SetCurrentTenantIdFunctionProducer)
    [106](https://github.com/starnowski/posmulten/issues/106)  
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSPolicyEnricher#setTableRLSPolicySQLDefinitionsProducer(TableRLSPolicySQLDefinitionsProducer)
    [106](https://github.com/starnowski/posmulten/issues/106)  
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.TableRLSSettingsSQLDefinitionsEnricher#setTableRLSSettingsSQLDefinitionsProducer(TableRLSSettingsSQLDefinitionsProducer)
    [106](https://github.com/starnowski/posmulten/issues/106)  
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantColumnSQLDefinitionsEnricher#setSingleTenantColumnSQLDefinitionsProducer(SingleTenantColumnSQLDefinitionsProducer)
    [106](https://github.com/starnowski/posmulten/issues/106) 
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantHasAuthoritiesFunctionDefinitionEnricher#setEqualsCurrentTenantIdentifierFunctionProducer(EqualsCurrentTenantIdentifierFunctionProducer)
    [106](https://github.com/starnowski/posmulten/issues/106) 
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.TenantHasAuthoritiesFunctionDefinitionEnricher#setTenantHasAuthoritiesFunctionProducer(TenantHasAuthoritiesFunctionProducer)
    [106](https://github.com/starnowski/posmulten/issues/106) 
    
-   Removed type com.github.starnowski.posmulten.postgresql.core.context.TenantColumnRequest
    [108](https://github.com/starnowski/posmulten/issues/108)   
-   Removed method com.github.starnowski.posmulten.postgresql.core.context.enrichers.IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher#setIsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer(IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsProducer)
    [108](https://github.com/starnowski/posmulten/issues/108)   