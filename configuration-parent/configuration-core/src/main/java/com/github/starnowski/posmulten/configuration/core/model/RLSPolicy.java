package com.github.starnowski.posmulten.configuration.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RLSPolicy {
    private String name;
    private String tenantColumn;
    private Map<String, String> primaryKeyColumnsNameToTypeMap;
    private Boolean createTenantColumnForTable;
    private String nameForFunctionThatChecksIfRecordExistsInTable;
    private String validTenantValueConstraintName;
    private Boolean skipAddingOfTenantColumnDefaultValue;
}
