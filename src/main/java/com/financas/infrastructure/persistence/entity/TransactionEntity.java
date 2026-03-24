package com.financas.infrastructure.persistence.entity;

import com.financas.domain.valueobject.TransactionStatus;
import com.financas.domain.valueobject.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "recurrence_id")
    private UUID recurrenceId;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String description;
    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean installment;

    int totalInstallments;

    int currentInstallment;
}
