package com.github.starnowski.posmulten.configuration.common.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry
import com.github.starnowski.posmulten.configuration.yaml.core.IConfigurationMapper
import com.github.starnowski.posmulten.configuration.yaml.core.model.AbstractCustomDefinitionEntry

import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.AT_BEGINNING
import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.CUSTOM

abstract class AbstractCustomDefinitionEntryMapperTest<T extends AbstractCustomDefinitionEntry<T>, M extends IConfigurationMapper<CustomDefinitionEntry, T>, CMTC extends AbstractConfigurationMapperTestContext> extends AbstractConfigurationMapperTest<T, CustomDefinitionEntry, M, CMTC> {
    @Override
    protected Class<CustomDefinitionEntry> getConfigurationObjectClass() {
        CustomDefinitionEntry.class
    }

    protected abstract T createOutputInstance();

    @Override
    protected List<T> prepareExpectedMappedObjectsList() {
        [createOutputInstance(),
         createOutputInstance().setCreationScript("C1"),
         createOutputInstance().setCreationScript("DDS").setDropScript("Script"),
         createOutputInstance().setCreationScript("DDS").setDropScript("Script").setValidationScripts(["select", "drop"]),
         createOutputInstance().setPosition(AT_BEGINNING),
         createOutputInstance().setPosition(CUSTOM).setCustomPosition("0")
        ]
    }

    @Override
    protected List<CustomDefinitionEntry> prepareExpectedUnmappeddObjectsList() {
        [new CustomDefinitionEntry(),
         new CustomDefinitionEntry().setCreationScript("C1"),
         new CustomDefinitionEntry().setCreationScript("DDS").setDropScript("Script"),
         new CustomDefinitionEntry().setCreationScript("DDS").setDropScript("Script").setValidationScripts(["select", "drop"]),
         new CustomDefinitionEntry().setPosition(AT_BEGINNING),
         new CustomDefinitionEntry().setPosition(CUSTOM).setCustomPosition("0")
        ]
    }
}
