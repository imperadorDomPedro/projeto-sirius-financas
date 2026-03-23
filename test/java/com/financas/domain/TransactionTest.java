package com.financas.domain;

import com.financas.domain.model.Transaction;
import com.financas.domain.valueobject.Money;
import com.financas.domain.valueobject.TransactionStatus;
import com.financas.domain.valueobject.TransactionType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class TransactionTest {

    @Test void shouldCreateExpenseTransaction() {
        Transaction t = Transaction.create(UUID.randomUUID(), null,
                Money.of(new BigDecimal("250.00")), TransactionType.EXPENSE,
                "Supermercado", LocalDate.now());
        assertThat(t.isExpense()).isTrue();
        assertThat(t.getStatus()).isEqualTo(TransactionStatus.CONFIRMED);
    }

    @Test void shouldCancelTransaction() {
        Transaction t = Transaction.create(UUID.randomUUID(), null,
                Money.of(new BigDecimal("100.00")), TransactionType.INCOME,
                "Salário", LocalDate.now());
        t.cancel();
        assertThat(t.isCancelled()).isTrue();
    }

    @Test void shouldThrowWhenCancellingAlreadyCancelled() {
        Transaction t = Transaction.create(UUID.randomUUID(), null,
                Money.of(new BigDecimal("100.00")), TransactionType.INCOME,
                "Salário", LocalDate.now());
        t.cancel();
        assertThatThrownBy(t::cancel).isInstanceOf(IllegalStateException.class);
    }

    @Test void shouldThrowWhenAmountIsNull() {
        assertThatThrownBy(() -> Transaction.create(UUID.randomUUID(), null,
                null, TransactionType.EXPENSE, "desc", LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
