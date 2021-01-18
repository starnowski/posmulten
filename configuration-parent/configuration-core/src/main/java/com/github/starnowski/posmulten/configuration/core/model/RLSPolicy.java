package com.github.starnowski.posmulten.configuration.core.model;

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
public class RLSPolicy {
    private String name;
    private String tenantColumn;
    private Boolean createTenantColumnForTable;
    private String validTenantValueConstraintName;
    private Boolean skipAddingOfTenantColumnDefaultValue;
    private PrimaryKeyDefinition primaryKeyDefinition;
}
