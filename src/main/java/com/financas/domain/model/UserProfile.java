package com.financas.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserProfile {

    private final UUID id;

    private final UUID userId;

    private String avatarUrl;

    private String currency;

    private String locale;

    private String theme;

    private UserProfile(UUID id, UUID userId, String avatarUrl,
                        String currency, String locale, String theme) {
        this.id = id;
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.currency = currency;
        this.locale = locale;
        this.theme = theme;
    }

    public static UserProfile create(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("UserId is required");
        return new UserProfile(UUID.randomUUID(), userId, null, "BRL", "pt-BR", "LIGHT");
    }

    public static UserProfile reconstitute(UUID id, UUID userId, String avatarUrl,
                                           String currency, String locale, String theme) {
        return new UserProfile(id, userId, avatarUrl, currency, locale, theme);
    }

    public void updatePreferences(String currency, String locale, String theme) {
        if (currency != null && !currency.isBlank()) this.currency = currency.toUpperCase().trim();
        if (locale != null && !locale.isBlank()) this.locale = locale.trim();
        if (theme != null && !theme.isBlank()) this.theme = theme.toUpperCase().trim();
    }

    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
