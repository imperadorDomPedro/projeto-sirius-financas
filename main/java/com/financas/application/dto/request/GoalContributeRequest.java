package com.financas.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoalContributeRequest {
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}
