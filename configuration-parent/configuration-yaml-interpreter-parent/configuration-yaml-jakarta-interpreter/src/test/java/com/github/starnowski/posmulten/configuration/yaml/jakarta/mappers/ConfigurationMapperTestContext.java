package com.github.starnowski.posmulten.configuration.yaml.jakarta.mappers;

import com.github.starnowski.posmulten.configuration.common.yaml.mappers.AbstractConfigurationMapperTestContext;
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.ForeignKeyConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.jakarta.model.TableEntry;

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
