package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration
import com.github.starnowski.posmulten.configuration.core.model.TableEntry
import com.github.starnowski.posmulten.configuration.core.model.ValidTenantValueConstraintConfiguration

import static java.util.Arrays.asList

class SharedSchemaContextConfigurationMapperTest extends AbstractConfigurationMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration, com.github.starnowski.posmulten.configuration.core.model.SharedSchemaContextConfiguration, SharedSchemaContextConfigurationMapper> {
    @Override
    protected Class<SharedSchemaContextConfiguration> getConfigurationObjectClass() {
        SharedSchemaContextConfiguration.class
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration.class
    }

    @Override
    protected SharedSchemaContextConfigurationMapper getTestedObject() {
        new SharedSchemaContextConfigurationMapper()
    }

    @Override
    protected List<com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration> prepareExpectedMappedObjectsList() {
        [
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration(),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setSetCurrentTenantIdFunctionName("set_current_f_t_id_fun_na"),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setSetCurrentTenantIdFunctionName("ggxx").setGrantee("db-user"),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setGrantee("db-user").setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(true),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setCurrentTenantIdentifierAsDefaultValueForTenantColumnInAllTables(false).setCurrentTenantIdProperty("ccte.property"),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setCurrentTenantIdProperty("posmulten.property").setDefaultTenantIdColumn("tenant_col_id"),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setDefaultTenantIdColumn("tenant").setCurrentTenantIdPropertyType("VARCHAR(127)"),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setCurrentTenantIdPropertyType("VARCHAR(127)").setEqualsCurrentTenantIdentifierFunctionName("equals_cur_t"),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setEqualsCurrentTenantIdentifierFunctionName("equals_cur_t").setGetCurrentTenantIdFunctionName("get_tenant"),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setGetCurrentTenantIdFunctionName("get_tenant").setSetCurrentTenantIdFunctionName("this_is_a_tenant"),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setSetCurrentTenantIdFunctionName("set_cur_tenant").setForceRowLevelSecurityForTableOwner(true),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setForceRowLevelSecurityForTableOwner(false).setValidTenantValueConstraint(new com.github.starnowski.posmulten.configuration.yaml.model.ValidTenantValueConstraintConfiguration().setTenantIdentifiersBlacklist(asList("Invalid"))),
                new com.github.starnowski.posmulten.configuration.yaml.model.SharedSchemaContextConfiguration().setTables(asList(new com.github.starnowski.posmulten.configuration.yaml.model.TableEntry().setName("table_1"))),
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
