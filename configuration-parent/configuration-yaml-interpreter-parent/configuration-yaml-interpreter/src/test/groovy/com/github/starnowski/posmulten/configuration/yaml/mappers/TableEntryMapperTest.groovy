package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractTableEntryMapperTest
import com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration
import com.github.starnowski.posmulten.configuration.yaml.model.RLSPolicy
import com.github.starnowski.posmulten.configuration.yaml.model.TableEntry

class TableEntryMapperTest extends AbstractTableEntryMapperTest<ForeignKeyConfiguration, RLSPolicy, TableEntry, TableEntryMapper, ConfigurationMapperTestContext> {

    @Override
    protected ConfigurationMapperTestContext getConfigurationMapperTestContext() {
        new ConfigurationMapperTestContext()
    }

    @Override
    protected Class<TableEntry> getYamlConfigurationObjectClass() {
        TableEntry.class
    }

    @Override
    protected TableEntryMapper getTestedObject() {
        new TableEntryMapper()
    }

    @Override
    protected TableEntry createOutputInstance() {
        new TableEntry()
    }

    @Override
    protected ForeignKeyConfiguration createForeignKeyConfigurationInstance() {
        new ForeignKeyConfiguration()
    }

    @Override
    protected RLSPolicy createRLSPolicyInstance() {
        new RLSPolicy()
    }
}
