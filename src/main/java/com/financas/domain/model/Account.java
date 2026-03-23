package com.financas.domain.model;

import com.financas.domain.valueobject.AccountType;
import com.financas.domain.valueobject.Money;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Account {

    private final UUID id;

    private final UUID userId;

    private String name;

    private AccountType type;

    private Money balance;

    private String color;

    private boolean active;

    private Account(UUID id, UUID userId, String name, AccountType type,
                    Money balance, String color, boolean active) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.color = color;
        this.active = active;
    }

    public static Account create(UUID userId, String name, AccountType type,
                                  Money initialBalance, String color) {
        if (userId == null) throw new IllegalArgumentException("User is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Account name is required");
        return new Account(UUID.randomUUID(), userId, name, type, initialBalance, color, true);
    }

    public static Account reconstitute(UUID id, UUID userId, String name, AccountType type,
                                        Money balance, String color, boolean active) {
        return new Account(id, userId, name, type, balance, color, active);
    }

    public void credit(Money amount) {
        this.balance = this.balance.add(amount);
    }

    public void debit(Money amount) {
        this.balance = this.balance.subtract(amount);
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        this.name = newName;
    }

    public void deactivate() { this.active = false; }
}
