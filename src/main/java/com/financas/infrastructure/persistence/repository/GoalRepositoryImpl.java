package com.financas.infrastructure.persistence.repository;

import com.financas.domain.model.Goal;
import com.financas.domain.repository.GoalRepository;
import com.financas.infrastructure.persistence.mapper.GoalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GoalRepositoryImpl implements GoalRepository {
    private final GoalJpaRepository jpa;
    private final GoalMapper mapper;

    @Override
    public Goal save(Goal g) {
        return mapper.toDomain(jpa.save(mapper.toEntity(g)));
    }

    @Override
    public Optional<Goal> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Goal> findByUserId(UUID userId) {
        return jpa.findByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
