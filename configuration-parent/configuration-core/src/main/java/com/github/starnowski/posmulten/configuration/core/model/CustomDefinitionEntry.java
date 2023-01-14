package com.github.starnowski.posmulten.configuration.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CustomDefinitionEntry {

    private CustomDefinitionPosition position;
    private String creationScript;
    private String dropScript;
    private String customPosition;
    private List<String> validationScripts;
    public enum CustomDefinitionPosition {
        AT_END,
        AT_BEGINNING,
        CUSTOM
    }
}
