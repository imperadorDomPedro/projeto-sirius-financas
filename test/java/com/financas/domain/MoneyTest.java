package com.financas.domain;

import com.financas.domain.valueobject.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

class MoneyTest {

    @Test void shouldAddTwoAmounts() {
        Money a = Money.of(new BigDecimal("100.00"));
        Money b = Money.of(new BigDecimal("50.00"));
        assertThat(a.add(b).amount()).isEqualByComparingTo("150.00");
    }

    @Test void shouldSubtractAmounts() {
        Money a = Money.of(new BigDecimal("100.00"));
        Money b = Money.of(new BigDecimal("30.00"));
        assertThat(a.subtract(b).amount()).isEqualByComparingTo("70.00");
    }

    @Test void shouldThrowWhenSubtractResultIsNegative() {
        Money a = Money.of(new BigDecimal("10.00"));
        Money b = Money.of(new BigDecimal("50.00"));
        assertThatThrownBy(() -> a.subtract(b)).isInstanceOf(IllegalStateException.class);
    }

    @Test void shouldRejectNullAmount() {
        assertThatThrownBy(() -> Money.of(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void shouldRejectNegativeAmount() {
        assertThatThrownBy(() -> Money.of(new BigDecimal("-1.00"))).isInstanceOf(IllegalArgumentException.class);
    }
}
