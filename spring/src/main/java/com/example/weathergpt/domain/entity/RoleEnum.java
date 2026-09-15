package com.example.weathergpt.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    NORMAL_USER("normal_user"),
    MARINE("marine"),
    AVIATION("aviation"),
    AGRICULTURE("agriculture"),
    RESEARCH("research");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
