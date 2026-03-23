package com.financas.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class User {

    private final UUID id;

    private String name;

    private final String email;

    private String passwordHash;

    private String currency;

    private boolean active;

    private final LocalDateTime createdAt;

    private User(UUID id, String name, String email, String passwordHash,
                 String currency, boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.currency = currency;
        this.active = active;
        this.createdAt = createdAt;
    }

    public static User create(String name, String email, String passwordHash) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Valid email is required");
        return new User(UUID.randomUUID(), name, email, passwordHash, "BRL", true, LocalDateTime.now());
    }

    public static User reconstitute(UUID id, String name, String email, String passwordHash,
                                     String currency, boolean active, LocalDateTime createdAt) {
        return new User(id, name, email, passwordHash, currency, active, createdAt);
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        this.name = name;
    }

    public void updatePassword(String newHash) { this.passwordHash = newHash; }
    public void deactivate() { this.active = false; }
}
