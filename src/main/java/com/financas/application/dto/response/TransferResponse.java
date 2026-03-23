package com.financas.application.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data @Builder
public class TransferResponse {
    private UUID id;

    private UUID originAccountId;

    private UUID destAccountId;

    private BigDecimal amount;

    private LocalDate transferDate;

    private String description;
}
