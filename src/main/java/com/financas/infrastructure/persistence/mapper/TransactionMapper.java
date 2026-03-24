package com.financas.infrastructure.persistence.mapper;

import com.financas.domain.model.Transaction;
import com.financas.domain.valueobject.Money;
import com.financas.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionEntity toEntity(Transaction t) {
        return TransactionEntity.builder()
                .id(t.getId()).accountId(t.getAccountId()).categoryId(t.getCategoryId())
                .amount(t.getAmount().amount())
                .type(t.getType()).description(t.getDescription())
                .transactionDate(t.getDate()).status(t.getStatus()).build();
    }

    public Transaction toDomain(TransactionEntity e) {
        return Transaction.reconstitute(e.getId(), e.getAccountId(), e.getUserId(),
                Money.of(e.getAmount()), e.getType(), e.getCategoryId(), e.getTransactionDate(),
                e.getDescription(), e.getStatus(), e.isInstallment(), e.getTotalInstallments(), e.getCurrentInstallment());
    }
}
