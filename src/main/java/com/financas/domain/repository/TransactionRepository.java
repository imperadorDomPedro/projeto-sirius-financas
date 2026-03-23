package com.financas.domain.repository;

import com.financas.domain.model.Transaction;
import com.financas.domain.valueobject.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByAccountId(UUID accountId);
    List<Transaction> findByUserIdAndDateBetween(UUID userId, LocalDate from, LocalDate to);
    List<Transaction> findByUserIdAndType(UUID userId, TransactionType type);
    List<Transaction> findByCategoryIdAndMonthYear(UUID categoryId, int month, int year);
    void deleteById(UUID id);
}
