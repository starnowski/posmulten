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
package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SharedSchemaContextConfiguration {

    @NotBlank
    @JsonProperty(value = "default_schema", required = true)
    private String defaultSchema;
    @Valid
    @JsonProperty(value = "current_tenant_id_property_type")
    private StringWrapperWithNotBlankValue currentTenantIdPropertyType;
    @Valid
    @JsonProperty(value = "current_tenant_id_property")
    private StringWrapperWithNotBlankValue currentTenantIdProperty;
    @Valid
    @JsonProperty(value = "get_current_tenant_id_function_name")
    private StringWrapperWithNotBlankValue getCurrentTenantIdFunctionName;
    @Valid
    @JsonProperty(value = "set_current_tenant_id_function_name")
    private StringWrapperWithNotBlankValue setCurrentTenantIdFunctionName;
    @Valid
    @JsonProperty(value = "equals_current_tenant_identifier_function_name")
    private StringWrapperWithNotBlankValue equalsCurrentTenantIdentifierFunctionName;
    @Valid
    @JsonProperty(value = "tenant_has_authorities_function_name")
    private StringWrapperWithNotBlankValue tenantHasAuthoritiesFunctionName;
    @JsonProperty(value = "force_row_level_security_for_table_owner")
    private Boolean forceRowLevelSecurityForTableOwner;
    @Valid
    @JsonProperty(value = "default_tenant_id_column")
    private StringWrapperWithNotBlankValue defaultTenantIdColumn;
    @NotBlank
    @JsonProperty(value = "grantee", required = true)
    private String grantee;
    @JsonProperty(value = "set_current_tenant_identifier_as_default_value_for_tenant_column_in_all_tables")
    private Boolean currentTenantIdentifierAsDefaultValueForTenantColumnInAllTables;
    @Valid
    @JsonProperty(value = "valid_tenant_value_constraint")
    private ValidTenantValueConstraintConfiguration validTenantValueConstraint;
    @JsonProperty(value = "tables")
    private List<@Valid TableEntry> tables;
    @Valid
    @JsonProperty(value = "sql_definitions_validation")
    private SqlDefinitionsValidation sqlDefinitionsValidation;
    @Valid
    @JsonProperty(value = "custom_sql_definitions")
    private List<CustomDefinitionEntry> customSQLDefinitions;

    public SharedSchemaContextConfiguration setCurrentTenantIdPropertyType(String currentTenantIdPropertyType) {
        this.currentTenantIdPropertyType = new StringWrapperWithNotBlankValue(currentTenantIdPropertyType);
        return this;
    }

    public SharedSchemaContextConfiguration setCurrentTenantIdProperty(String currentTenantIdProperty) {
        this.currentTenantIdProperty = new StringWrapperWithNotBlankValue(currentTenantIdProperty);
        return this;
    }

    public SharedSchemaContextConfiguration setGetCurrentTenantIdFunctionName(String getCurrentTenantIdFunctionName) {
        this.getCurrentTenantIdFunctionName = new StringWrapperWithNotBlankValue(getCurrentTenantIdFunctionName);
        return this;
    }

    public SharedSchemaContextConfiguration setSetCurrentTenantIdFunctionName(String setCurrentTenantIdFunctionName) {
        this.setCurrentTenantIdFunctionName = new StringWrapperWithNotBlankValue(setCurrentTenantIdFunctionName);
        return this;
    }

    public SharedSchemaContextConfiguration setEqualsCurrentTenantIdentifierFunctionName(String equalsCurrentTenantIdentifierFunctionName) {
        this.equalsCurrentTenantIdentifierFunctionName = new StringWrapperWithNotBlankValue(equalsCurrentTenantIdentifierFunctionName);
        return this;
    }

    public SharedSchemaContextConfiguration setDefaultTenantIdColumn(String defaultTenantIdColumn) {
        this.defaultTenantIdColumn = new StringWrapperWithNotBlankValue(defaultTenantIdColumn);
        return this;
    }

    public SharedSchemaContextConfiguration setCurrentTenantIdPropertyType(StringWrapperWithNotBlankValue currentTenantIdPropertyType) {
        this.currentTenantIdPropertyType = currentTenantIdPropertyType;
        return this;
    }

    public SharedSchemaContextConfiguration setCurrentTenantIdProperty(StringWrapperWithNotBlankValue currentTenantIdProperty) {
        this.currentTenantIdProperty = currentTenantIdProperty;
        return this;
    }

    public SharedSchemaContextConfiguration setGetCurrentTenantIdFunctionName(StringWrapperWithNotBlankValue getCurrentTenantIdFunctionName) {
        this.getCurrentTenantIdFunctionName = getCurrentTenantIdFunctionName;
        return this;
    }

    public SharedSchemaContextConfiguration setSetCurrentTenantIdFunctionName(StringWrapperWithNotBlankValue setCurrentTenantIdFunctionName) {
        this.setCurrentTenantIdFunctionName = setCurrentTenantIdFunctionName;
        return this;
    }

    public SharedSchemaContextConfiguration setEqualsCurrentTenantIdentifierFunctionName(StringWrapperWithNotBlankValue equalsCurrentTenantIdentifierFunctionName) {
        this.equalsCurrentTenantIdentifierFunctionName = equalsCurrentTenantIdentifierFunctionName;
        return this;
    }

    public SharedSchemaContextConfiguration setTenantHasAuthoritiesFunctionName(StringWrapperWithNotBlankValue tenantHasAuthoritiesFunctionName) {
        this.tenantHasAuthoritiesFunctionName = tenantHasAuthoritiesFunctionName;
        return this;
    }

    public SharedSchemaContextConfiguration setDefaultTenantIdColumn(StringWrapperWithNotBlankValue defaultTenantIdColumn) {
        this.defaultTenantIdColumn = defaultTenantIdColumn;
        return this;
    }

    public SharedSchemaContextConfiguration withCustomSQLDefinitions(List<CustomDefinitionEntry> customSQLDefinitions) {
        this.customSQLDefinitions = customSQLDefinitions;
        return this;
    }
}
