package com.financas.infrastructure.persistence.mapper;

import com.financas.domain.model.User;
import com.financas.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(User u) {
        return UserEntity.builder()
                .id(u.getId()).name(u.getName()).email(u.getEmail())
                .passwordHash(u.getPasswordHash()).active(u.isActive())
                .createdAt(u.getCreatedAt()).build();
    }

    public User toDomain(UserEntity e) {
        return User.reconstitute(e.getId(), e.getName(), e.getEmail(),
                e.getPasswordHash(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
