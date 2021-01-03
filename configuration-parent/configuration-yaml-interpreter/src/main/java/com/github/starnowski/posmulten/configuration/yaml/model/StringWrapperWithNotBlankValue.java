package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class StringWrapperWithNotBlankValue {

    private final String value;

    @JsonCreator
    public StringWrapperWithNotBlankValue(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
