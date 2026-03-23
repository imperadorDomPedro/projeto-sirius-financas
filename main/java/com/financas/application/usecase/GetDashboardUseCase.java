package com.financas.application.usecase;

import com.financas.application.dto.response.DashboardResponse;
import com.financas.domain.model.Transaction;
import com.financas.domain.repository.BudgetRepository;
import com.financas.domain.repository.GoalRepository;
import com.financas.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetDashboardUseCase {

    private final TransactionRepository transactionRepository;

    private final BudgetRepository budgetRepository;

    private final GoalRepository goalRepository;

    private final ManageBudgetUseCase manageBudgetUseCase;

    private final ManageGoalUseCase manageGoalUseCase;

    @Transactional(readOnly = true)
    public DashboardResponse execute(UUID userId, int month, int year) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());

        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, from, to);

        BigDecimal totalIncome = transactions.stream()
                .filter(Transaction::isIncome)
                .filter(Transaction::isConfirmed)
                .map(t -> t.getAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(Transaction::isExpense)
                .filter(Transaction::isConfirmed)
                .map(t -> t.getAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        BigDecimal savingsRate = totalIncome.compareTo(BigDecimal.ZERO) > 0
                ? balance.divide(totalIncome, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        var budgetAlerts = budgetRepository.findByUserIdAndMonthAndYear(userId, month, year)
                .stream()
                .filter(b -> b.getUsagePercent() >= 80)
                .map(b -> manageBudgetUseCase.toResponse(b, null))
                .toList();

        var activeGoals = goalRepository.findByUserId(userId).stream()
                .filter(g -> g.getStatus() == com.financas.domain.valueobject.GoalStatus.IN_PROGRESS)
                .map(manageGoalUseCase::toResponse)
                .toList();

        var recentTransactions = transactions.stream()
                .sorted((a, b) -> b.getTransactionDate().compareTo(a.getTransactionDate()))
                .limit(10)
                .map(CreateTransactionUseCase::toResponse)
                .toList();

        return DashboardResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .savingsRate(savingsRate)
                .budgetAlerts(budgetAlerts)
                .activeGoals(activeGoals)
                .recentTransactions(recentTransactions)
                .build();
    }
}
