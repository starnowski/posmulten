package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode
public final class StringWrapperWithNotBlankValue {

    @NotBlank
    private final String value;

    @JsonCreator
    public StringWrapperWithNotBlankValue(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static StringWrapperWithNotBlankValue valueOf(String value)
    {
        return new StringWrapperWithNotBlankValue(value);
    }
}
