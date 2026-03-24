package com.financas.domain.model;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Permission {

    private final UUID id;

    private final String code;       // Ex: TRANSACTION:CREATE

    private final String module;     // Ex: TRANSACTION

    private final String action;     // Ex: CREATE

    private final String description;

    private Permission(UUID id, String code, String module, String action, String description) {
        this.id = id;
        this.code = code;
        this.module = module;
        this.action = action;
        this.description = description;
    }

    public static Permission create(String module, String action, String description) {
        if (module == null || module.isBlank()) throw new IllegalArgumentException("Module is required");
        if (action == null || action.isBlank()) throw new IllegalArgumentException("Action is required");

        String normalizedModule = module.toUpperCase().trim();
        String normalizedAction = action.toUpperCase().trim();
        String code = normalizedModule + ":" + normalizedAction;

        return new Permission(UUID.randomUUID(), code, normalizedModule, normalizedAction, description);
    }

    public static Permission reconstitute(UUID id, String code, String module,
                                          String action, String description) {
        return new Permission(id, code, module, action, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
