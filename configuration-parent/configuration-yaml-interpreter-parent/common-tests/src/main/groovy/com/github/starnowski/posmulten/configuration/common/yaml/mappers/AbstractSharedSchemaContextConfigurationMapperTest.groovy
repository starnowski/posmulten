package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractSharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractTableEntry
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractValidTenantValueConstraintConfiguration

import static java.util.Arrays.asList

abstract class AbstractSharedSchemaContextConfigurationMapperTest <TE extends AbstractTableEntry, VTVCC extends AbstractValidTenantValueConstraintConfiguration, T extends AbstractSharedSchemaContextConfiguration<?, ?, VTVCC, TE, ?, T> , M extends IConfigurationMapper< SharedSchemaContextConfiguration, T> , CMTC extends AbstractConfigurationMapperTestContext> extends AbstractConfigurationMapperTest<T, SharedSchemaContextConfiguration, M, CMTC> {
    @Override
    protected Class<SharedSchemaContextConfiguration> getConfigurationObjectClass() {
        SharedSchemaContextConfiguration.class
    }

    protected abstract T createOutputInstance()
    protected abstract VTVCC createValidTenantValueConstraintConfigurationInstance()
    protected abstract TE createTableEntryInstance()

    @Override
    protected List<T> prepareExpectedMappedObjectsList() {
        [
                createOutputInstance(),
                createOutputInstance().setSetCurrentTenantIdFunctionName("set_current_f_t_id_fun_na"),
                createOutputInstance().setSetCurrentTenantIdFunctionName("ggxx").setGrantee("db-user"),
                createOutputInstance().setGrantee("db-user").setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true),
                createOutputInstance().setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(false).setCurrentTenantIdProperty("ccte.property"),
                createOutputInstance().setCurrentTenantIdProperty("posmulten.property").setDefaultTenantIdColumn("tenant_col_id"),
                createOutputInstance().setDefaultTenantIdColumn("tenant").setCurrentTenantIdPropertyType("VARCHAR(127)"),
                createOutputInstance().setCurrentTenantIdPropertyType("VARCHAR(127)").setEqualsCurrentTenantIdentifierFunctionName("equals_cur_t"),
                createOutputInstance().setEqualsCurrentTenantIdentifierFunctionName("equals_cur_t").setGetCurrentTenantIdFunctionName("get_tenant"),
                createOutputInstance().setGetCurrentTenantIdFunctionName("get_tenant").setSetCurrentTenantIdFunctionName("this_is_a_tenant"),
                createOutputInstance().setSetCurrentTenantIdFunctionName("set_cur_tenant").setForceRowLevelSecurityForTableOwner(true),
                createOutputInstance().setForceRowLevelSecurityForTableOwner(false).setValidTenantValueConstraint(createValidTenantValueConstraintConfigurationInstance().setTenantIdentifiersBlacklist(asList("Invalid"))),
                createOutputInstance().setTables(asList(createTableEntryInstance().setName("table_1"))),
        ]
    }

    @Override
    protected List<SharedSchemaContextConfiguration> prepareExpectedUnmappeddObjectsList() {
        [
                new SharedSchemaContextConfiguration(),
                new SharedSchemaContextConfiguration().setSetCurrentTenantIdFunctionName("set_current_f_t_id_fun_na"),
                new SharedSchemaContextConfiguration().setSetCurrentTenantIdFunctionName("ggxx").setGrantee("db-user"),
                new SharedSchemaContextConfiguration().setGrantee("db-user").setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true),
                new SharedSchemaContextConfiguration().setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(false).setCurrentTenantIdProperty("ccte.property"),
                new SharedSchemaContextConfiguration().setCurrentTenantIdProperty("posmulten.property").setDefaultTenantIdColumn("tenant_col_id"),
                new SharedSchemaContextConfiguration().setDefaultTenantIdColumn("tenant").setCurrentTenantIdPropertyType("VARCHAR(127)"),
                new SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("VARCHAR(127)").setEqualsCurrentTenantIdentifierFunctionName("equals_cur_t"),
                new SharedSchemaContextConfiguration().setEqualsCurrentTenantIdentifierFunctionName("equals_cur_t").setGetCurrentTenantIdFunctionName("get_tenant"),
                new SharedSchemaContextConfiguration().setGetCurrentTenantIdFunctionName("get_tenant").setSetCurrentTenantIdFunctionName("this_is_a_tenant"),
                new SharedSchemaContextConfiguration().setSetCurrentTenantIdFunctionName("set_cur_tenant").setForceRowLevelSecurityForTableOwner(true),
                new SharedSchemaContextConfiguration().setForceRowLevelSecurityForTableOwner(false).setValidTenantValueConstraint(new ValidTenantValueConstraintConfiguration().setTenantIdentifiersBlacklist(asList("Invalid"))),
                new SharedSchemaContextConfiguration().setTables(asList(new TableEntry().setName("table_1"))),
        ]
    }
}
