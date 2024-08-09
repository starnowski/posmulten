package com.github.starnowski.posmulten.configuration.yaml.mappers

import com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry

import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.AT_BEGINNING
import static com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry.CustomDefinitionPosition.CUSTOM

class CustomDefinitionEntryMapperTest extends AbstractConfigurationMapperTest<com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry, com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry, CustomDefinitionEntryMapper> {
    @Override
    protected Class<CustomDefinitionEntry> getConfigurationObjectClass() {
        CustomDefinitionEntry.class
    }

    @Override
    protected Class<com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry> getYamlConfigurationObjectClass() {
        com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry.class
    }

    @Override
    protected CustomDefinitionEntryMapper getTestedObject() {
        new CustomDefinitionEntryMapper()
    }

    @Override
    protected List<com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry> prepareExpectedMappedObjectsList() {
        [new com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry(),
         new com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry().setCreationScript("C1"),
         new com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry().setCreationScript("DDS").setDropScript("Script"),
         new com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry().setCreationScript("DDS").setDropScript("Script").setValidationScripts(["select", "drop"]),
         new com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry().setPosition(AT_BEGINNING),
         new com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry().setPosition(CUSTOM).setCustomPosition("0")
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
