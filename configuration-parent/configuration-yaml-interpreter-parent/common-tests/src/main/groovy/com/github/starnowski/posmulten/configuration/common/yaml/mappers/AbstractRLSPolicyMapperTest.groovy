package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.PrimaryKeyDefinition
import com.github.starnowski.posmulten.configuration.core.model.RLSPolicy
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractPrimaryKeyDefinition
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractRLSPolicy

import static com.github.starnowski.posmulten.postgresql.test.utils.MapBuilder.mapBuilder

abstract class AbstractRLSPolicyMapperTest <PK extends AbstractPrimaryKeyDefinition<PK>, T extends AbstractRLSPolicy<?, PK, T>, M extends IConfigurationMapper<RLSPolicy, T> , CMTC extends AbstractConfigurationMapperTestContext> extends AbstractConfigurationMapperTest<T, RLSPolicy, M, CMTC> {

    @Override
    protected Class<RLSPolicy> getConfigurationObjectClass() {
        RLSPolicy.class
    }

    protected abstract T createOutputInstance()
    protected abstract PK createPrimaryKeyDefinitionInstance()

    @Override
    protected List<T> prepareExpectedMappedObjectsList() {
        [
                createOutputInstance(),
                createOutputInstance().setName("_some_policy"),
                createOutputInstance().setName("rls_1").setSkipAddingOfTenantColumnDefaultValue(true),
                createOutputInstance().setName("policy_2").setSkipAddingOfTenantColumnDefaultValue(false),
                createOutputInstance().setValidTenantValueConstraintName("constr_1").setCreateTenantColumnForTable(true),
                createOutputInstance().setValidTenantValueConstraintName("wrong_tenant_const").setPrimaryKeyDefinition(createPrimaryKeyDefinitionInstance().setNameForFunctionThatChecksIfRecordExistsInTable("record_is_valid")),
                createOutputInstance().setTenantColumn("ten_col").setName("posts").setPrimaryKeyDefinition(createPrimaryKeyDefinitionInstance().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").build())),
                createOutputInstance().setName("posts").setPrimaryKeyDefinition(createPrimaryKeyDefinitionInstance().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("some_uuid", "UUID").build()))

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
                new RLSPolicy().setValidTenantValueConstraintName("wrong_tenant_const").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setNameForFunctionThatChecksIfRecordExistsInTable("record_is_valid")),
                new RLSPolicy().setTenantColumn("ten_col").setName("posts").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").build())),
                new RLSPolicy().setName("posts").setPrimaryKeyDefinition(new PrimaryKeyDefinition().setPrimaryKeyColumnsNameToTypeMap(mapBuilder().put("id", "int").put("some_uuid", "UUID").build()))

        ]
    }
}
