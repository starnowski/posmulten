package com.github.starnowski.posmulten.configuration.yaml.jakarta.dao


import com.github.starnowski.posmulten.configuration.common.yaml.dao.AbstractSharedSchemaContextConfigurationYamlDaoValidationTest

class SharedSchemaContextConfigurationYamlDaoValidationTest extends AbstractSharedSchemaContextConfigurationYamlDaoValidationTest<SharedSchemaContextConfigurationYamlDao> {

    @Override
    protected SharedSchemaContextConfigurationYamlDao getSharedSchemaContextConfigurationYamlDao() {
        new SharedSchemaContextConfigurationYamlDao()
    }
}