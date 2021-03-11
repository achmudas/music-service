package com.music.models.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WrapperType {

    ARTIST("artist");

    private final String value;

    WrapperType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static WrapperType fromString(String value) {
        return value == null ? null : WrapperType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
