package com.financas.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SystemParameter {

    private final UUID id;

    private final String key;

    private String value;

    private String description;

    private final boolean editable;

    private SystemParameter(UUID id, String key, String value, String description, boolean editable) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.description = description;
        this.editable = editable;
    }

    public static SystemParameter create(String key, String value, String description, boolean editable) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("Key is required");
        if (value == null) throw new IllegalArgumentException("Value cannot be null");

        return new SystemParameter(UUID.randomUUID(), key.toUpperCase().trim(),
                value, description, editable);
    }

    public static SystemParameter reconstitute(UUID id, String key, String value,
                                               String description, boolean editable) {
        return new SystemParameter(id, key, value, description, editable);
    }

    public void updateValue(String newValue) {
        if (!this.editable) throw new IllegalStateException("Parameter '" + key + "' is not editable");
        if (newValue == null) throw new IllegalArgumentException("Value cannot be null");
        this.value = newValue;
    }

    public boolean asBoolean() {
        return Boolean.parseBoolean(this.value);
    }

    public int asInt() {
        try {
            return Integer.parseInt(this.value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Parameter '" + key + "' is not a valid integer: " + value);
        }
    }
}
