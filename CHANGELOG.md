# Posmulten changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

https://keepachangelog.com/en/1.0.0/
https://changelog.com/podcast/127


## [Unreleased]

## [0.2.0] - 2020-11-14
### Added

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
        
-   Throwing an exception in IsRecordBelongsToCurrentTenantConstraintSQLDefinitionsEnricher class when the mapping of 
    foreign keys and primary keys are not correct with the RLS policy declaration for the table. 
    [113](https://github.com/starnowski/posmulten/issues/113)
    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#getValidatorsCopy() 
        [113](https://github.com/starnowski/posmulten/issues/113)    
    -   Added method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#setValidators(List<ISharedSchemaContextRequestValidator>) 
        [113](https://github.com/starnowski/posmulten/issues/113)

### Changed

-   Renamed method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createRLSPolicyForTable(String, Map<String, String>, String, String)
    from createRLSPolicyForColumn to createRLSPolicyForTable [113](https://github.com/starnowski/posmulten/issues/113)