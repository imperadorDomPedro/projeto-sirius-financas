package com.financas.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateTransferRequest {
    @NotNull
    private UUID originAccountId;

    @NotNull
    private UUID destAccountId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull
    private LocalDate transferDate;

    @Size(max = 255)
    private String description;
}
