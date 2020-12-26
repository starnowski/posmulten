package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SharedSchemaContextConfiguration {

    @JsonProperty(value = "default_schema", required = true)
    private String defaultSchema;
    @JsonProperty(value = "current_tenant_id_property_type")
    private String currentTenantIdPropertyType;
    @JsonProperty(value = "current_tenant_id_property")
    private String currentTenantIdProperty;
    @JsonProperty(value = "get_current_tenant_id_function_name")
    private String getCurrentTenantIdFunctionName;
    @JsonProperty(value = "set_current_tenant_id_function_name")
    private String setCurrentTenantIdFunctionName;
}
