package com.financas.domain.model;

import com.financas.domain.valueobject.Money;
import com.financas.domain.valueobject.TransactionStatus;
import com.financas.domain.valueobject.TransactionType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Transaction {

    private final UUID id;

    private final UUID accountId;

    private final UUID userId;

    private final Money amount;

    private final TransactionType type;

    private final UUID categoryId;

    private LocalDate date;

    private String description;

    private TransactionStatus status;

    private final boolean installment;

    private final int totalInstallments;

    private final int currentInstallment;

    private Transaction(UUID id, UUID accountId, UUID userId, Money amount,
                        TransactionType type, UUID categoryId, LocalDate date,
                        String description, TransactionStatus status,
                        boolean installment, int totalInstallments, int currentInstallment) {
        this.id = id;
        this.accountId = accountId;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.categoryId = categoryId;
        this.date = date;
        this.description = description;
        this.status = status;
        this.installment = installment;
        this.totalInstallments = totalInstallments;
        this.currentInstallment = currentInstallment;
    }

    public static Transaction create(UUID accountId, UUID userId, Money amount,
                                     TransactionType type, UUID categoryId,
                                     LocalDate date, String description) {
        if (accountId == null) throw new IllegalArgumentException("AccountId is required");
        if (userId == null) throw new IllegalArgumentException("UserId is required");
        if (amount == null) throw new IllegalArgumentException("Amount is required");
        if (amount.isZero()) throw new IllegalArgumentException("Amount cannot be zero");
        if (type == null) throw new IllegalArgumentException("Transaction type is required");
        if (date == null) throw new IllegalArgumentException("Date is required");

        return new Transaction(UUID.randomUUID(), accountId, userId, amount, type,
                categoryId, date, description, TransactionStatus.CONFIRMED,
                false, 1, 1);
    }

    public static Transaction createInstallment(UUID accountId, UUID userId, Money amount,
                                                TransactionType type, UUID categoryId,
                                                LocalDate date, String description,
                                                int totalInstallments, int currentInstallment) {
        if (totalInstallments < 2) throw new IllegalArgumentException("Installments must be at least 2");
        if (currentInstallment < 1 || currentInstallment > totalInstallments)
            throw new IllegalArgumentException("Current installment out of range");

        return new Transaction(UUID.randomUUID(), accountId, userId, amount, type,
                categoryId, date, description, TransactionStatus.CONFIRMED,
                true, totalInstallments, currentInstallment);
    }

    public static Transaction reconstitute(UUID id, UUID accountId, UUID userId, Money amount,
                                           TransactionType type, UUID categoryId, LocalDate date,
                                           String description, TransactionStatus status,
                                           boolean installment, int totalInstallments, int currentInstallment) {
        return new Transaction(id, accountId, userId, amount, type, categoryId, date,
                description, status, installment, totalInstallments, currentInstallment);
    }

    public void confirm() {
        if (this.status == TransactionStatus.CANCELLED)
            throw new IllegalStateException("Cannot confirm a cancelled transaction");
        this.status = TransactionStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == TransactionStatus.CANCELLED)
            throw new IllegalStateException("Transaction is already cancelled");
        this.status = TransactionStatus.CANCELLED;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void reschedule(LocalDate newDate) {
        if (newDate == null) throw new IllegalArgumentException("Date cannot be null");
        if (this.status == TransactionStatus.CANCELLED)
            throw new IllegalStateException("Cannot reschedule a cancelled transaction");
        this.date = newDate;
    }

    public boolean isExpense() { return this.type == TransactionType.EXPENSE; }

    public boolean isIncome() { return this.type == TransactionType.INCOME; }

    public boolean isPending() {
        return this.status == TransactionStatus.PENDING;
    }

    public boolean isCancelled() {
        return this.status == TransactionStatus.CANCELLED;
    }

    public boolean isConfirmed() {
        return this.status == TransactionStatus.CONFIRMED;
    }


}
