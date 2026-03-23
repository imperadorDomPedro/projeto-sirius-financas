package com.financas.application.dto.response;

import com.financas.domain.valueobject.AccountType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder
public class AccountResponse {
    private UUID id;

    private String name;

    private AccountType type;

    private BigDecimal balance;

    private String color;

    private boolean active;
}
