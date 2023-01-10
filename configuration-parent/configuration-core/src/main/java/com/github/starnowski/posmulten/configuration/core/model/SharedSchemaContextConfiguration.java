package com.github.starnowski.posmulten.configuration.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SharedSchemaContextConfiguration {

    private String defaultSchema;
    private String currentTenantIdPropertyType;
    private String currentTenantIdProperty;
    private String getCurrentTenantIdFunctionName;
    private String setCurrentTenantIdFunctionName;
    private String equalsCurrentTenantIdentifierFunctionName;
    private String tenantHasAuthoritiesFunctionName;
    private Boolean forceRowLevelSecurityForTableOwner;
    private String defaultTenantIdColumn;
    private String grantee;
    private Boolean currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables;
    private ValidTenantValueConstraintConfiguration validTenantValueConstraint;
    private List<TableEntry> tables;
    private SqlDefinitionsValidation sqlDefinitionsValidation;
    private List<CustomDefinitionEntry> customDefinitions;
}
