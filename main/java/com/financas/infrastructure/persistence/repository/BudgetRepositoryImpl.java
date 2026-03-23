package com.financas.infrastructure.persistence.repository;

import com.financas.domain.model.Budget;
import com.financas.domain.repository.BudgetRepository;
import com.financas.infrastructure.persistence.mapper.BudgetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetRepository {
    private final BudgetJpaRepository jpa;
    private final BudgetMapper mapper;

    @Override
    public Budget save(Budget b) {
        return mapper.toDomain(jpa.save(mapper.toEntity(b)));
    }

    @Override
    public Optional<Budget> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Budget> findByUserIdAndCategoryIdAndMonthAndYear(UUID userId, UUID categoryId, int month, int year) {
        return jpa.findByUserIdAndCategoryIdAndMonthAndYear(userId, categoryId, month, year).map(mapper::toDomain);
    }

    @Override
    public List<Budget> findByUserIdAndMonthAndYear(UUID userId, int month, int year) {
        return jpa.findByUserIdAndMonthAndYear(userId, month, year).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
