package com.github.starnowski.posmulten.configuration.yaml.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class StringWrapper {

    private final String value;

    @JsonCreator
    public StringWrapper(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
