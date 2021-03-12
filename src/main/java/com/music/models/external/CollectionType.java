package com.music.models.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CollectionType {

    ALBUM("Album");

    private final String value;

    CollectionType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static CollectionType fromString(String value) {
        return value == null ? null : CollectionType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
