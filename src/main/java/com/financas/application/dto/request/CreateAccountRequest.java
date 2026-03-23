package com.financas.application.dto.request;

import com.financas.domain.valueobject.AccountType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    @NotBlank
    private String name;
    @NotNull
    private AccountType type;
    @NotNull @DecimalMin("0.00")
    private BigDecimal initialBalance;
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String color;
}
