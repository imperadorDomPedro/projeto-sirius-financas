package com.financas.application.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder
public class BudgetResponse {
    private UUID id;

    private UUID categoryId;

    private String categoryName;

    private BigDecimal limitAmount;

    private BigDecimal spentAmount;

    private BigDecimal remainingAmount;

    private double usagePercent;

    private boolean exceeded;

    private int month;

    private int year;
}
