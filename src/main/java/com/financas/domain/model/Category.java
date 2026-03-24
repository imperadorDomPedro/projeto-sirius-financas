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

    private final UUID parentId; // null = categoria raiz

    private boolean active;

    private Category(UUID id, UUID userId, String name, CategoryType type,
                     String color, String icon, UUID parentId, boolean active) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.color = color;
        this.icon = icon;
        this.parentId = parentId;
        this.active = active;
    }

    public static Category create(UUID userId, String name, CategoryType type,
                                  String color, String icon, UUID parentId) {
        if (userId == null) throw new IllegalArgumentException("UserId is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Category name is required");
        if (type == null) throw new IllegalArgumentException("Category type is required");

        return new Category(UUID.randomUUID(), userId, name.trim(), type,
                color, icon, parentId, true);
    }

    public static Category reconstitute(UUID id, UUID userId, String name, CategoryType type,
                                        String color, String icon, UUID parentId, boolean active) {
        return new Category(id, userId, name, type, color, icon, parentId, active);
    }

    public boolean isSubcategory() {
        return this.parentId != null;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        this.name = newName.trim();
    }

    public void updateAppearance(String color, String icon) {
        if (color != null && !color.isBlank()) this.color = color;
        if (icon != null && !icon.isBlank()) this.icon = icon;
    }

    public void deactivate() {
        this.active = false;
    }
}
