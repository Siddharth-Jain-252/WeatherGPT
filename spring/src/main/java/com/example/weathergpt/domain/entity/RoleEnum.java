package com.example.weathergpt.domain.entity;

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

    public String getValue() {
        return value;
    }
}
