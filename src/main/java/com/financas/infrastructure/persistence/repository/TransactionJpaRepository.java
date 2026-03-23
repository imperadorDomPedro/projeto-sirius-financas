package com.financas.infrastructure.persistence.repository;

import com.financas.domain.valueobject.TransactionType;
import com.financas.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByAccountId(UUID accountId);

    @Query("""
                SELECT t FROM TransactionEntity t
                JOIN AccountEntity a ON t.accountId = a.id
                WHERE a.userId = :userId
                  AND t.transactionDate BETWEEN :from AND :to
                ORDER BY t.transactionDate DESC
            """)
    List<TransactionEntity> findByUserIdAndDateBetween(
            @Param("userId") UUID userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    @Query("""
                SELECT t FROM TransactionEntity t
                JOIN AccountEntity a ON t.accountId = a.id
                WHERE a.userId = :userId AND t.type = :type
            """)
    List<TransactionEntity> findByUserIdAndType(@Param("userId") UUID userId, @Param("type") TransactionType type);

    @Query("""
                SELECT t FROM TransactionEntity t
                WHERE t.categoryId = :categoryId
                  AND MONTH(t.transactionDate) = :month
                  AND YEAR(t.transactionDate) = :year
            """)
    List<TransactionEntity> findByCategoryIdAndMonthYear(
            @Param("categoryId") UUID categoryId,
            @Param("month") int month,
            @Param("year") int year);
}
