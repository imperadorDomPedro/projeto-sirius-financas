package com.financas.domain.model;

import com.financas.domain.valueobject.AccountType;
import com.financas.domain.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account")
class AccountTest {

    private UUID userId;
    private Money initialBalance;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        initialBalance = Money.of(new BigDecimal("1000.00"));
    }

    // -------------------------------------------------------------------------
    // create()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("deve criar conta com dados válidos")
        void shouldCreateAccountWithValidData() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING, initialBalance, "#8A05BE");

            assertNotNull(account.getId());
            assertEquals(userId, account.getUserId());
            assertEquals("Nubank", account.getName());
            assertEquals(AccountType.CHECKING, account.getType());
            assertEquals(initialBalance, account.getBalance());
            assertEquals("#8A05BE", account.getColor());
            assertTrue(account.isActive());
        }

        @Test
        @DisplayName("deve gerar UUID único a cada criação")
        void shouldGenerateUniqueId() {
            Account a1 = Account.create(userId, "Conta 1", AccountType.CHECKING, initialBalance, "#FFF");
            Account a2 = Account.create(userId, "Conta 2", AccountType.CHECKING, initialBalance, "#FFF");

            assertNotEquals(a1.getId(), a2.getId());
        }

        @Test
        @DisplayName("deve lançar exceção quando userId é nulo")
        void shouldThrowWhenUserIdIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Account.create(null, "Nubank", AccountType.CHECKING, initialBalance, "#FFF"));

            assertEquals("User is required", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando name é nulo")
        void shouldThrowWhenNameIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Account.create(userId, null, AccountType.CHECKING, initialBalance, "#FFF"));

            assertEquals("Account name is required", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando name é em branco")
        void shouldThrowWhenNameIsBlank() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                    Account.create(userId, "   ", AccountType.CHECKING, initialBalance, "#FFF"));

            assertEquals("Account name is required", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // reconstitute()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("reconstitute()")
    class Reconstitute {

        @Test
        @DisplayName("deve reconstituir conta com todos os dados preservados")
        void shouldReconstituteWithAllData() {
            UUID id = UUID.randomUUID();
            Account account = Account.reconstitute(id, userId, "Poupança", AccountType.SAVINGS,
                    initialBalance, "#00FF00", false);

            assertEquals(id, account.getId());
            assertEquals(userId, account.getUserId());
            assertEquals("Poupança", account.getName());
            assertEquals(AccountType.SAVINGS, account.getType());
            assertEquals(initialBalance, account.getBalance());
            assertEquals("#00FF00", account.getColor());
            assertFalse(account.isActive());
        }
    }

    // -------------------------------------------------------------------------
    // credit()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("credit()")
    class Credit {

        @Test
        @DisplayName("deve somar o valor ao saldo existente")
        void shouldAddAmountToBalance() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING, initialBalance, "#FFF");
            Money credit = Money.of(new BigDecimal("500.00"));

            account.credit(credit);

            assertEquals(Money.of(new BigDecimal("1500.00")), account.getBalance());
        }

        @Test
        @DisplayName("deve acumular múltiplos créditos corretamente")
        void shouldAccumulateMultipleCredits() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING,
                    Money.of(BigDecimal.ZERO), "#FFF");

            account.credit(Money.of(new BigDecimal("100.00")));
            account.credit(Money.of(new BigDecimal("200.00")));
            account.credit(Money.of(new BigDecimal("50.00")));

            assertEquals(Money.of(new BigDecimal("350.00")), account.getBalance());
        }
    }

    // -------------------------------------------------------------------------
    // debit()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("debit()")
    class Debit {

        @Test
        @DisplayName("deve subtrair o valor do saldo existente")
        void shouldSubtractAmountFromBalance() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING, initialBalance, "#FFF");
            Money debit = Money.of(new BigDecimal("300.00"));

            account.debit(debit);

            assertEquals(Money.of(new BigDecimal("700.00")), account.getBalance());
        }

        @Test
        @DisplayName("deve lançar exceção ao debitar valor maior que o saldo")
        void shouldThrowWhenDebitExceedsBalance() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING,
                    Money.of(new BigDecimal("100.00")), "#FFF");

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> account.debit(Money.of(new BigDecimal("200.00"))));

            assertEquals("Insufficient funds: result would be negative", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // rename()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("rename()")
    class Rename {

        @Test
        @DisplayName("deve atualizar o nome da conta")
        void shouldUpdateName() {
            Account account = Account.create(userId, "Antigo", AccountType.CHECKING, initialBalance, "#FFF");

            account.rename("Novo Nome");

            assertEquals("Novo Nome", account.getName());
        }

        @Test
        @DisplayName("deve lançar exceção quando novo nome é nulo")
        void shouldThrowWhenNewNameIsNull() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING, initialBalance, "#FFF");

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> account.rename(null));

            assertEquals("Name cannot be blank", ex.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando novo nome é em branco")
        void shouldThrowWhenNewNameIsBlank() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING, initialBalance, "#FFF");

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> account.rename("  "));

            assertEquals("Name cannot be blank", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // deactivate()
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("deactivate()")
    class Deactivate {

        @Test
        @DisplayName("deve marcar conta como inativa")
        void shouldMarkAccountAsInactive() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING, initialBalance, "#FFF");
            assertTrue(account.isActive());

            account.deactivate();

            assertFalse(account.isActive());
        }

        @Test
        @DisplayName("deve ser idempotente — chamar duas vezes não causa erro")
        void shouldBeIdempotent() {
            Account account = Account.create(userId, "Nubank", AccountType.CHECKING, initialBalance, "#FFF");

            account.deactivate();
            assertDoesNotThrow(account::deactivate);
            assertFalse(account.isActive());
        }
    }
}