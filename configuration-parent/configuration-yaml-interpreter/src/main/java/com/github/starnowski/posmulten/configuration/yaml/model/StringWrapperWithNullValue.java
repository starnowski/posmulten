package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class StringWrapperWithNullValue {

    private final String value;

    @JsonCreator
    public StringWrapperWithNullValue(String value) {
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

    public static StringWrapperWithNullValue valueOf(String value)
    {
        return new StringWrapperWithNullValue(value);
    }
}
