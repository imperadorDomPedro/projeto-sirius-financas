package com.financas.domain.model;

import com.financas.domain.valueobject.GoalStatus;
import com.financas.domain.valueobject.Money;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Goal {

    private final UUID id;

    private final UUID userId;

    private String name;

    private Money targetAmount;

    private Money currentAmount;

    private LocalDate deadline;

    private GoalStatus status;

    private Goal(UUID id, UUID userId, String name, Money targetAmount,
                 Money currentAmount, LocalDate deadline, GoalStatus status) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.status = status;
    }

    public static Goal create(UUID userId, String name, Money target, LocalDate deadline) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Goal name is required");
        return new Goal(UUID.randomUUID(), userId, name, target, Money.zero(), deadline, GoalStatus.IN_PROGRESS);
    }

    public static Goal reconstitute(UUID id, UUID userId, String name, Money target,
                                     Money current, LocalDate deadline, GoalStatus status) {
        return new Goal(id, userId, name, target, current, deadline, status);
    }

    public void contribute(Money amount) {
        if (status != GoalStatus.IN_PROGRESS) throw new IllegalStateException("Goal is not in progress");
        this.currentAmount = this.currentAmount.add(amount);
        if (!this.currentAmount.isGreaterThan(this.targetAmount) &&
            this.currentAmount.equals(this.targetAmount)) {
            this.status = GoalStatus.COMPLETED;
        }
    }

    public double getProgressPercent() {
        if (targetAmount.isZero()) return 0.0;
        return currentAmount.amount()
                .divide(targetAmount.amount(), 4, java.math.RoundingMode.HALF_UP)
                .multiply(java.math.BigDecimal.valueOf(100))
                .min(java.math.BigDecimal.valueOf(100))
                .doubleValue();
    }

    public boolean isCompleted() { return status == GoalStatus.COMPLETED; }

    public void cancel() { this.status = GoalStatus.CANCELLED; }
}
