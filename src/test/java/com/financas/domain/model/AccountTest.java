package com.financas.domain.model;

import com.financas.domain.valueobject.AccountType;
import com.financas.domain.valueobject.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void shouldCreditBalance() {
        Account a = Account.create(UUID.randomUUID(), "Nubank", AccountType.CHECKING,
                Money.of(new BigDecimal("100.00")), "#8A05BE");
        a.credit(Money.of(new BigDecimal("200.00")));
        assertEquals(Money.of(new BigDecimal("300.00")), a.getBalance());
    }

    @Test
    void shouldDebitBalance() {
        Account a = Account.create(UUID.randomUUID(), "Nubank", AccountType.CHECKING,
                Money.of(new BigDecimal("500.00")), "#8A05BE");
        a.debit(Money.of(new BigDecimal("150.00")));
        assertEquals(new BigDecimal("350.00"), a.getBalance().amount());
    }

    @Test
    void shouldThrowWhenDebitExceedsBalance() {
        Account a = Account.create(UUID.randomUUID(), "Nubank", AccountType.CHECKING,
                Money.of(new BigDecimal("100.00")), "#8A05BE");

        assertThrows(IllegalStateException.class,
                () -> a.debit(Money.of(new BigDecimal("200.00")))
        );
    }
}
