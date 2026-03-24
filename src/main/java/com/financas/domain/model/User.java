package com.financas.domain.model;

import com.financas.domain.valueobject.UserStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class User {

    private final UUID id;

    private String email;

    private String passwordHash;

    private String name;

    private UserStatus status;

    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private User(UUID id, String email, String passwordHash, String name,
                 UserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String email, String passwordHash, String name) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");
        if (!email.contains("@")) throw new IllegalArgumentException("Email is invalid");
        if (passwordHash == null || passwordHash.isBlank()) throw new IllegalArgumentException("Password is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");

        LocalDateTime now = LocalDateTime.now();
        return new User(UUID.randomUUID(), email.toLowerCase().trim(), passwordHash, name.trim(),
                UserStatus.ACTIVE, now, now);
    }

    public static User reconstitute(UUID id, String email, String passwordHash, String name,
                                    UserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new User(id, email, passwordHash, name, status, createdAt, updatedAt);
    }

    public void updateName(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        this.name = newName.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEmail(String newEmail) {
        if (newEmail == null || newEmail.isBlank()) throw new IllegalArgumentException("Email cannot be blank");
        if (!newEmail.contains("@")) throw new IllegalArgumentException("Email is invalid");
        this.email = newEmail.toLowerCase().trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.isBlank())
            throw new IllegalArgumentException("Password cannot be blank");
        this.passwordHash = newPasswordHash;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void block() {
        this.status = UserStatus.BLOCKED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }
}
