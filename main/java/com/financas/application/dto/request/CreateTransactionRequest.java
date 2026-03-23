package com.financas.application.dto.request;

import com.financas.domain.valueobject.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateTransactionRequest {

    @NotNull(message = "Account is required")
    private UUID accountId;

    private UUID categoryId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 13, fraction = 2)
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @Size(max = 255)
    private String description;

    @NotNull(message = "Date is required")
    private LocalDate transactionDate;
}
