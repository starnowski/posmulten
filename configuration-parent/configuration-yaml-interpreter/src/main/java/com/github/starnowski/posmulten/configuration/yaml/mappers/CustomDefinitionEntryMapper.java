package com.github.starnowski.posmulten.configuration.yaml.mappers;

import com.github.starnowski.posmulten.configuration.yaml.IConfigurationMapper;
import com.github.starnowski.posmulten.configuration.yaml.model.CustomDefinitionEntry;

public class CustomDefinitionEntryMapper implements IConfigurationMapper<com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry, CustomDefinitionEntry> {
    @Override
    public CustomDefinitionEntry map(com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry input) {
        return new CustomDefinitionEntry().setCreationScript(input.getCreationScript()).setDropScript(input.getDropScript())
                .setPosition(input.getPosition()).setCustomPosition(input.getCustomPosition()).setValidationScripts(input.getValidationScripts());
    }

    @Override
    public com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry unmap(CustomDefinitionEntry output) {
        return new com.github.starnowski.posmulten.configuration.core.model.CustomDefinitionEntry().setCreationScript(output.getCreationScript())
                .setDropScript(output.getDropScript()).setPosition(output.getPosition()).setCustomPosition(output.getCustomPosition())
                .setValidationScripts(output.getValidationScripts());
    }
}
