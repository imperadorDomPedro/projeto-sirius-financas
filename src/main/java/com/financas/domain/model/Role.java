package com.financas.domain.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Role {

    private final UUID id;

    private String name;

    private String description;

    private final List<Permission> permissions;

    private Role(UUID id, String name, String description, List<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = new ArrayList<>(permissions);
    }

    public static Role create(String name, String description) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Role name is required");
        return new Role(UUID.randomUUID(), name.toUpperCase().trim(), description, List.of());
    }

    public static Role reconstitute(UUID id, String name, String description, List<Permission> permissions) {
        return new Role(id, name, description, permissions);
    }

    public void addPermission(Permission permission) {
        if (permission == null) throw new IllegalArgumentException("Permission cannot be null");
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }

    public boolean hasPermission(String permissionCode) {
        return permissions.stream()
                .anyMatch(p -> p.getCode().equalsIgnoreCase(permissionCode));
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }
}
