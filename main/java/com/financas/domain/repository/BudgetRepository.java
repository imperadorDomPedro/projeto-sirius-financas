package com.financas.domain.repository;

import com.financas.domain.model.Budget;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository {
    Budget save(Budget budget);
    Optional<Budget> findById(UUID id);
    Optional<Budget> findByUserIdAndCategoryIdAndMonthAndYear(UUID userId, UUID categoryId, int month, int year);
    List<Budget> findByUserIdAndMonthAndYear(UUID userId, int month, int year);
    void deleteById(UUID id);
}
