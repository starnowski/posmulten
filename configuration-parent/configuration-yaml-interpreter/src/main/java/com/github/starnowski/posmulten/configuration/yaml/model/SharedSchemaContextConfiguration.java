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
    @JsonProperty(value = "current_tenant_id_property_type")
    private StringWrapperWithNotBlankValue currentTenantIdPropertyType;
    @JsonProperty(value = "current_tenant_id_property")
    private StringWrapperWithNotBlankValue currentTenantIdProperty;
    @JsonProperty(value = "get_current_tenant_id_function_name")
    private StringWrapperWithNotBlankValue getCurrentTenantIdFunctionName;
    @JsonProperty(value = "set_current_tenant_id_function_name")
    private StringWrapperWithNotBlankValue setCurrentTenantIdFunctionName;
    @JsonProperty(value = "equals_current_tenant_identifier_function_name")
    private StringWrapperWithNotBlankValue equalsCurrentTenantIdentifierFunctionName;
    @JsonProperty(value = "tenant_has_authorities_function_name")
    private StringWrapperWithNotBlankValue tenantHasAuthoritiesFunctionName;
    @JsonProperty(value = "force_row_level_security_for_table_owner")
    private Boolean forceRowLevelSecurityForTableOwner;
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

    public SharedSchemaContextConfiguration setTenantHasAuthoritiesFunctionName(String tenantHasAuthoritiesFunctionName) {
        this.tenantHasAuthoritiesFunctionName = new StringWrapperWithNotBlankValue(tenantHasAuthoritiesFunctionName);
        return this;
    }

    public SharedSchemaContextConfiguration setDefaultTenantIdColumn(String defaultTenantIdColumn) {
        this.defaultTenantIdColumn = new StringWrapperWithNotBlankValue(defaultTenantIdColumn);
        return this;
    }
}
