package com.financas.infrastructure.persistence.repository;

import com.financas.infrastructure.persistence.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GoalJpaRepository extends JpaRepository<GoalEntity, UUID> {
    List<GoalEntity> findByUserId(UUID userId);
}
