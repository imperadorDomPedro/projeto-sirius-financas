package com.financas.application.dto.response;

import com.financas.domain.valueobject.TransactionStatus;
import com.financas.domain.valueobject.TransactionType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data @Builder
public class TransactionResponse {
    private UUID id;

    private UUID accountId;

    private UUID categoryId;

    private BigDecimal amount;

    private TransactionType type;

    private String description;

    private LocalDate transactionDate;

    private TransactionStatus status;
}
