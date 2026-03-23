package com.financas.domain.model;

import com.financas.domain.valueobject.Money;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Budget {

    private final UUID id;

    private final UUID userId;

    private final UUID categoryId;

    private Money limitAmount;

    private Money spentAmount;

    private final int month;

    private final int year;

    private Budget(UUID id, UUID userId, UUID categoryId,
                   Money limitAmount, Money spentAmount, int month, int year) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.limitAmount = limitAmount;
        this.spentAmount = spentAmount;
        this.month = month;
        this.year = year;
    }

    public static Budget create(UUID userId, UUID categoryId, Money limit, int month, int year) {
        validateMonthYear(month, year);
        return new Budget(UUID.randomUUID(), userId, categoryId, limit, Money.zero(), month, year);
    }

    public static Budget reconstitute(UUID id, UUID userId, UUID categoryId,
                                       Money limit, Money spent, int month, int year) {
        return new Budget(id, userId, categoryId, limit, spent, month, year);
    }

    public void addSpending(Money amount) {
        this.spentAmount = this.spentAmount.add(amount);
    }

    public boolean isExceeded() {
        return this.spentAmount.isGreaterThan(this.limitAmount);
    }

    public Money getRemainingAmount() {
        if (isExceeded()) return Money.zero();
        return this.limitAmount.subtract(this.spentAmount);
    }

    public double getUsagePercent() {
        if (limitAmount.isZero()) return 0.0;
        return spentAmount.amount()
                .divide(limitAmount.amount(), 4, java.math.RoundingMode.HALF_UP)
                .multiply(java.math.BigDecimal.valueOf(100))
                .doubleValue();
    }

    private static void validateMonthYear(int month, int year) {
        if (month < 1 || month > 12) throw new IllegalArgumentException("Invalid month: " + month);
        if (year < 2000 || year > 2100) throw new IllegalArgumentException("Invalid year: " + year);
    }
}
