package com.financas.infrastructure.persistence.repository;

import com.financas.domain.model.Transaction;
import com.financas.domain.repository.TransactionRepository;
import com.financas.domain.valueobject.TransactionType;
import com.financas.infrastructure.persistence.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {
    private final TransactionJpaRepository jpa;
    private final TransactionMapper mapper;

    @Override
    public Transaction save(Transaction t) {
        return mapper.toDomain(jpa.save(mapper.toEntity(t)));
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        return jpa.findByAccountId(accountId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Transaction> findByUserIdAndDateBetween(UUID userId, LocalDate from, LocalDate to) {
        return jpa.findByUserIdAndDateBetween(userId, from, to).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Transaction> findByUserIdAndType(UUID userId, TransactionType type) {
        return jpa.findByUserIdAndType(userId, type).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Transaction> findByCategoryIdAndMonthYear(UUID categoryId, int month, int year) {
        return jpa.findByCategoryIdAndMonthYear(categoryId, month, year).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
