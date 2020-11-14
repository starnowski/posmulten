# Posmulten changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

https://keepachangelog.com/en/1.0.0/
https://changelog.com/podcast/127


## [Unreleased]

## [0.2.0] - 2020-11-14
### Added

-   Add components that generate constraint on tenant column that checks if tenant value is valid and generate function 
    that checks if passed tenant value is not one of values from blacklist [106](https://github.com/starnowski/posmulten/issues/106)

    -   Added type com.github.starnowski.posmulten.postgresql.core.common.function.DefaultFunctionArgumentValueToStringMapper


### Changed

-   Renamed method com.github.starnowski.posmulten.postgresql.core.context.DefaultSharedSchemaContextBuilder#createRLSPolicyForTable(String, Map<String, String>, String, String)
    from createRLSPolicyForColumn to createRLSPolicyForTable [113](https://github.com/starnowski/posmulten/issues/113)