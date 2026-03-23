package com.financas.domain.model;

import com.financas.domain.valueobject.CategoryType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Category {

    private final UUID id;

    private final UUID userId;

    private String name;

    private CategoryType type;

    private String color;

    private String icon;

    private UUID parentId;

    private Category(UUID id, UUID userId, String name, CategoryType type,
                     String color, String icon, UUID parentId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.color = color;
        this.icon = icon;
        this.parentId = parentId;
    }

    public static Category create(UUID userId, String name, CategoryType type, String color, String icon) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Category name is required");
        return new Category(UUID.randomUUID(), userId, name, type, color, icon, null);
    }

    public static Category reconstitute(UUID id, UUID userId, String name, CategoryType type,
                                         String color, String icon, UUID parentId) {
        return new Category(id, userId, name, type, color, icon, parentId);
    }

    public void update(String name, String color, String icon) {
        if (name != null && !name.isBlank()) this.name = name;
        if (color != null) this.color = color;
        if (icon != null) this.icon = icon;
    }
}
