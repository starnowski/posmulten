package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractConfigurationMapperTestContext;
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractForeignKeyConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractTableEntry;
import com.github.starnowski.posmulten.configuration.yaml.model.ForeignKeyConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.model.TableEntry;

public class ConfigurationMapperTestContext implements AbstractConfigurationMapperTestContext<ForeignKeyConfiguration, TableEntry> {

    @Override
    public Class<ForeignKeyConfiguration> getForeignKeyConfigurationClass() {
        return ForeignKeyConfiguration.class;
    }

    @Override
    public Class<TableEntry> getTableEntryClass() {
        return TableEntry.class;
    }
}
