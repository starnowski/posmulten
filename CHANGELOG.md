# Posmulten changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

https://keepachangelog.com/en/1.0.0/
https://changelog.com/podcast/127
https://www.markdownguide.org/basic-syntax/

## [Unreleased]

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