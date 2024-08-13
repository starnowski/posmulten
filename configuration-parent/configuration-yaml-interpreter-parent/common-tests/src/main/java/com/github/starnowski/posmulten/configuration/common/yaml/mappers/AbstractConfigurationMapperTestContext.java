package com.github.starnowski.posmulten.configuration.common.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractForeignKeyConfiguration;
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractTableEntry;

public interface AbstractConfigurationMapperTestContext<FKC extends AbstractForeignKeyConfiguration, TE extends AbstractTableEntry> {

    Class<FKC> getForeignKeyConfigurationClass();
    Class<TE> getTableEntryClass();
}
