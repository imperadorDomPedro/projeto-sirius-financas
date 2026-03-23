package com.financas.domain.repository;

import com.financas.domain.model.Goal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository {
    Goal save(Goal goal);
    Optional<Goal> findById(UUID id);
    List<Goal> findByUserId(UUID userId);
    void deleteById(UUID id);
}
