package com.financas.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateGoalRequest {
    @NotBlank
    private String name;

    @NotNull @DecimalMin("0.01")
    private BigDecimal targetAmount;

    private LocalDate deadline;
}
