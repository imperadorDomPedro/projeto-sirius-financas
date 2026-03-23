package com.financas.domain;

import com.financas.domain.model.Account;
import com.financas.domain.valueobject.AccountType;
import com.financas.domain.valueobject.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class AccountTest {

    @Test void shouldCreditBalance() {
        Account a = Account.create(UUID.randomUUID(), "Nubank", AccountType.CHECKING,
                Money.of(new BigDecimal("100.00")), "#8A05BE");
        a.credit(Money.of(new BigDecimal("200.00")));
        assertThat(a.getBalance().amount()).isEqualByComparingTo("300.00");
    }

    @Test void shouldDebitBalance() {
        Account a = Account.create(UUID.randomUUID(), "Nubank", AccountType.CHECKING,
                Money.of(new BigDecimal("500.00")), "#8A05BE");
        a.debit(Money.of(new BigDecimal("150.00")));
        assertThat(a.getBalance().amount()).isEqualByComparingTo("350.00");
    }

    @Test void shouldThrowWhenDebitExceedsBalance() {
        Account a = Account.create(UUID.randomUUID(), "Nubank", AccountType.CHECKING,
                Money.of(new BigDecimal("100.00")), "#8A05BE");
        assertThatThrownBy(() -> a.debit(Money.of(new BigDecimal("200.00"))))
                .isInstanceOf(IllegalStateException.class);
    }
}
