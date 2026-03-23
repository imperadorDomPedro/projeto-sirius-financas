package com.financas.domain.model;

import com.financas.domain.valueobject.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class BudgetTest {

    @Test void shouldNotExceedWhenSpentIsLow() {
        Budget b = Budget.create(UUID.randomUUID(), UUID.randomUUID(),
                Money.of(new BigDecimal("1000.00")), 3, 2026);
        b.addSpending(Money.of(new BigDecimal("400.00")));
        assertThat(b.isExceeded()).isFalse();
        assertThat(b.getRemainingAmount().amount()).isEqualByComparingTo("600.00");
    }

    @Test void shouldDetectExceededBudget() {
        Budget b = Budget.create(UUID.randomUUID(), UUID.randomUUID(),
                Money.of(new BigDecimal("500.00")), 3, 2026);
        b.addSpending(Money.of(new BigDecimal("600.00")));
        assertThat(b.isExceeded()).isTrue();
        assertThat(b.getUsagePercent()).isGreaterThan(100.0);
    }
}
