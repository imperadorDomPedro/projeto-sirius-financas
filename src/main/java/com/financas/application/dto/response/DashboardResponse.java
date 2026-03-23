package com.financas.application.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class DashboardResponse {
    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal balance;

    private BigDecimal savingsRate;

    private List<BudgetResponse> budgetAlerts;

    private List<GoalResponse> activeGoals;

    private List<TransactionResponse> recentTransactions;
}
