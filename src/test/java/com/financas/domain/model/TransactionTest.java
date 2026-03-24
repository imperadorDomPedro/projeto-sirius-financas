package com.financas.domain.model;

import com.financas.domain.valueobject.Money;
import com.financas.domain.valueobject.TransactionStatus;
import com.financas.domain.valueobject.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transaction")
class TransactionTest {

    private UUID accountId;
    private UUID userId;
    private UUID categoryId;
    private Money amount;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        accountId  = UUID.randomUUID();
        userId     = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        amount     = Money.of(new BigDecimal("250.00"));
        date       = LocalDate.of(2025, 1, 15);
    }

    // -------------------------------------------------------------------------
    // create()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("deve criar transação com dados válidos")
        void shouldCreateWithValidData() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "Mercado");

            assertNotNull(tx.getId());
            assertEquals(accountId, tx.getAccountId());
            assertEquals(userId, tx.getUserId());
            assertEquals(amount, tx.getAmount());
            assertEquals(TransactionType.EXPENSE, tx.getType());
            assertEquals(categoryId, tx.getCategoryId());
            assertEquals(date, tx.getDate());
            assertEquals("Mercado", tx.getDescription());
            assertEquals(TransactionStatus.CONFIRMED, tx.getStatus());
            assertFalse(tx.isInstallment());
            assertEquals(1, tx.getTotalInstallments());
            assertEquals(1, tx.getCurrentInstallment());
        }

        @Test
        @DisplayName("deve gerar UUID único a cada criação")
        void shouldGenerateUniqueId() {
            Transaction tx1 = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "A");
            Transaction tx2 = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "B");

            assertNotEquals(tx1.getId(), tx2.getId());
        }

        @Test
        @DisplayName("deve permitir categoryId nulo")
        void shouldAllowNullCategoryId() {
            assertDoesNotThrow(() ->
                    Transaction.create(accountId, userId, amount,
                            TransactionType.INCOME, null, date, "Salário"));
        }

        @Test
        @DisplayName("deve lançar exceção quando accountId é nulo")
        void shouldThrowWhenAccountIdIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.create(null, userId, amount, TransactionType.EXPENSE, categoryId, date, "X"));
            assertEquals("AccountId is required", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando userId é nulo")
        void shouldThrowWhenUserIdIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.create(accountId, null, amount, TransactionType.EXPENSE, categoryId, date, "X"));
            assertEquals("UserId is required", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando amount é nulo")
        void shouldThrowWhenAmountIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.create(accountId, userId, null, TransactionType.EXPENSE, categoryId, date, "X"));
            assertEquals("Amount is required", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando amount é zero")
        void shouldThrowWhenAmountIsZero() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.create(accountId, userId, Money.ZERO,
                            TransactionType.EXPENSE, categoryId, date, "X"));
            assertEquals("Amount cannot be zero", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando type é nulo")
        void shouldThrowWhenTypeIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.create(accountId, userId, amount, null, categoryId, date, "X"));
            assertEquals("Transaction type is required", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando date é nula")
        void shouldThrowWhenDateIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.create(accountId, userId, amount, TransactionType.EXPENSE, categoryId, null, "X"));
            assertEquals("Date is required", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // createInstallment()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("createInstallment()")
    class CreateInstallment {

        @Test
        @DisplayName("deve criar transação parcelada com dados válidos")
        void shouldCreateInstallmentWithValidData() {
            Transaction tx = Transaction.createInstallment(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "Notebook 3/12", 12, 3);

            assertTrue(tx.isInstallment());
            assertEquals(12, tx.getTotalInstallments());
            assertEquals(3, tx.getCurrentInstallment());
            assertEquals(TransactionStatus.CONFIRMED, tx.getStatus());
        }

        @Test
        @DisplayName("deve lançar exceção quando totalInstallments menor que 2")
        void shouldThrowWhenTotalInstallmentsLessThan2() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.createInstallment(accountId, userId, amount,
                            TransactionType.EXPENSE, categoryId, date, "X", 1, 1));
            assertEquals("Installments must be at least 2", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando currentInstallment é zero")
        void shouldThrowWhenCurrentInstallmentIsZero() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.createInstallment(accountId, userId, amount,
                            TransactionType.EXPENSE, categoryId, date, "X", 6, 0));
            assertEquals("Current installment out of range", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando currentInstallment maior que total")
        void shouldThrowWhenCurrentInstallmentExceedsTotal() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Transaction.createInstallment(accountId, userId, amount,
                            TransactionType.EXPENSE, categoryId, date, "X", 6, 7));
            assertEquals("Current installment out of range", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // reconstitute()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("reconstitute()")
    class Reconstitute {

        @Test
        @DisplayName("deve reconstituir transação com todos os dados preservados")
        void shouldReconstituteWithAllData() {
            UUID id = UUID.randomUUID();
            Transaction tx = Transaction.reconstitute(id, accountId, userId, amount,
                    TransactionType.INCOME, categoryId, date, "Freelance",
                    TransactionStatus.PENDING, false, 1, 1);

            assertEquals(id, tx.getId());
            assertEquals(TransactionStatus.PENDING, tx.getStatus());
            assertEquals(TransactionType.INCOME, tx.getType());
            assertTrue(tx.isPending());
        }
    }

    // -------------------------------------------------------------------------
    // confirm()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("confirm()")
    class Confirm {

        @Test
        @DisplayName("deve confirmar transação pendente")
        void shouldConfirmPendingTransaction() {
            Transaction tx = Transaction.reconstitute(UUID.randomUUID(), accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "X",
                    TransactionStatus.PENDING, false, 1, 1);

            tx.confirm();

            assertTrue(tx.isConfirmed());
        }

        @Test
        @DisplayName("deve lançar exceção ao confirmar transação cancelada")
        void shouldThrowWhenConfirmingCancelledTransaction() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "X");
            tx.cancel();

            IllegalStateException ex = assertThrows(IllegalStateException.class, tx::confirm);
            assertEquals("Cannot confirm a cancelled transaction", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // cancel()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("cancel()")
    class Cancel {

        @Test
        @DisplayName("deve cancelar transação confirmada")
        void shouldCancelConfirmedTransaction() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "X");

            tx.cancel();

            assertTrue(tx.isCancelled());
        }

        @Test
        @DisplayName("deve lançar exceção ao cancelar transação já cancelada")
        void shouldThrowWhenCancellingAlreadyCancelledTransaction() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "X");
            tx.cancel();

            IllegalStateException ex = assertThrows(IllegalStateException.class, tx::cancel);
            assertEquals("Transaction is already cancelled", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // updateDescription()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("updateDescription()")
    class UpdateDescription {

        @Test
        @DisplayName("deve atualizar a descrição")
        void shouldUpdateDescription() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "Antiga");

            tx.updateDescription("Nova descrição");

            assertEquals("Nova descrição", tx.getDescription());
        }

        @Test
        @DisplayName("deve permitir descrição nula")
        void shouldAllowNullDescription() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "Antiga");

            assertDoesNotThrow(() -> tx.updateDescription(null));
            assertNull(tx.getDescription());
        }
    }

    // -------------------------------------------------------------------------
    // reschedule()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("reschedule()")
    class Reschedule {

        @Test
        @DisplayName("deve reagendar a data da transação")
        void shouldRescheduleDate() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "X");
            LocalDate newDate = LocalDate.of(2025, 3, 1);

            tx.reschedule(newDate);

            assertEquals(newDate, tx.getDate());
        }

        @Test
        @DisplayName("deve lançar exceção ao reagendar com data nula")
        void shouldThrowWhenDateIsNull() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "X");

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> tx.reschedule(null));
            assertEquals("Date cannot be null", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção ao reagendar transação cancelada")
        void shouldThrowWhenReschedulingCancelledTransaction() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "X");
            tx.cancel();

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> tx.reschedule(LocalDate.now()));
            assertEquals("Cannot reschedule a cancelled transaction", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // isExpense() / isIncome()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("type helpers")
    class TypeHelpers {

        @Test
        @DisplayName("deve retornar true para isExpense quando type é EXPENSE")
        void shouldReturnTrueForExpense() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.EXPENSE, categoryId, date, "X");
            assertTrue(tx.isExpense());
            assertFalse(tx.isIncome());
        }

        @Test
        @DisplayName("deve retornar true para isIncome quando type é INCOME")
        void shouldReturnTrueForIncome() {
            Transaction tx = Transaction.create(accountId, userId, amount,
                    TransactionType.INCOME, categoryId, date, "Salário");
            assertTrue(tx.isIncome());
            assertFalse(tx.isExpense());
        }
    }
}
