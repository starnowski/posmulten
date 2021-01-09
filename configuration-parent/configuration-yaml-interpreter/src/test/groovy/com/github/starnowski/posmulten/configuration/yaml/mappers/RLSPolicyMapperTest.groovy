package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

class RLSPolicyMapperTest extends AbstractConfigurationMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy, com.github.starnowski.posmulten.configuration.core.model.RLSPolicy, RLSPolicyMapper> {

    @Override
    protected Class<RLSPolicy> getConfigurationObjectClass() {
        RLSPolicy.class
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy.class
    }

    @Override
    protected RLSPolicyMapper getTestedObject() {
        new RLSPolicyMapper()
    }

    @Override
    protected List<com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy> prepareExpectedMappedObjectsList() {
        [
                new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy(),
                new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy().setName("_some_policy"),
                new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy().setName("rls_1").setSkipAddingOfTenantColumnDefaultValue(true),
                new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy().setName("policy_2").setSkipAddingOfTenantColumnDefaultValue(false),
                new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy().setValidTenantValueConstraintName("constr_1").setCreateTenantColumnForTable(true),
                new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy().setValidTenantValueConstraintName("wrong_tenant_const").setNameForFunctionThatChecksIfRecordExistsInTable("record_is_valid"),
                new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy().setTenantColumn("ten_col").setName("posts").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").build()),
                new com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy().setName("posts").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("some_uuid", "UUID").build())

        ]
    }

    @Override
    protected List<RLSPolicy> prepareExpectedUnmappeddObjectsList() {
        [
                new RLSPolicy(),
                new RLSPolicy().setName("_some_policy"),
                new RLSPolicy().setName("rls_1").setSkipAddingOfTenantColumnDefaultValue(true),
                new RLSPolicy().setName("policy_2").setSkipAddingOfTenantColumnDefaultValue(false),
                new RLSPolicy().setValidTenantValueConstraintName("constr_1").setCreateTenantColumnForTable(true),
                new RLSPolicy().setValidTenantValueConstraintName("wrong_tenant_const").setNameForFunctionThatChecksIfRecordExistsInTable("record_is_valid"),
                new RLSPolicy().setTenantColumn("ten_col").setName("posts").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").build()),
                new RLSPolicy().setName("posts").setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("some_uuid", "UUID").build())

        ]
    }
}
