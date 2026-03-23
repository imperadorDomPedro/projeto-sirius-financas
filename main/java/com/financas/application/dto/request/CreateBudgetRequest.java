package com.financas.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateBudgetRequest {
    @NotNull
    private UUID categoryId;

    @NotNull @DecimalMin("0.01")
    private BigDecimal limitAmount;

    @NotNull @Min(1) @Max(12)
    private Integer month;

    @NotNull @Min(2000) @Max(2100)
    private Integer year;
}
