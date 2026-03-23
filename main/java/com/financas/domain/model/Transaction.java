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

    private UUID categoryId;

    private UUID recurrenceId;

    private Money amount;

    private TransactionType type;

    private String description;

    private LocalDate transactionDate;

    private TransactionStatus status;

    private Transaction(UUID id, UUID accountId, UUID categoryId, UUID recurrenceId,
                        Money amount, TransactionType type, String description,
                        LocalDate transactionDate, TransactionStatus status) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.recurrenceId = recurrenceId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    public static Transaction create(UUID accountId, UUID categoryId, Money amount,
                                     TransactionType type, String description, LocalDate date) {
        if (accountId == null) throw new IllegalArgumentException("Account is required");
        if (amount == null || amount.isZero()) throw new IllegalArgumentException("Amount must be greater than zero");
        if (type == null) throw new IllegalArgumentException("Transaction type is required");
        if (date == null) throw new IllegalArgumentException("Date is required");

        return new Transaction(UUID.randomUUID(), accountId, categoryId, null,
                amount, type, description, date, TransactionStatus.CONFIRMED);
    }

    public static Transaction reconstitute(UUID id, UUID accountId, UUID categoryId, UUID recurrenceId,
                                            Money amount, TransactionType type, String description,
                                            LocalDate date, TransactionStatus status) {
        return new Transaction(id, accountId, categoryId, recurrenceId, amount, type, description, date, status);
    }

    public void cancel() {
        if (this.status == TransactionStatus.CANCELLED) {
            throw new IllegalStateException("Transaction is already cancelled");
        }
        this.status = TransactionStatus.CANCELLED;
    }

    public void confirm() {
        if (this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Only pending transactions can be confirmed");
        }
        this.status = TransactionStatus.CONFIRMED;
    }

    public boolean isExpense()  { return this.type == TransactionType.EXPENSE; }
    public boolean isIncome()   { return this.type == TransactionType.INCOME; }
    public boolean isConfirmed(){ return this.status == TransactionStatus.CONFIRMED; }
    public boolean isCancelled(){ return this.status == TransactionStatus.CANCELLED; }
}
