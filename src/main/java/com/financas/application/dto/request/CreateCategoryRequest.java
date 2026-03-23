package com.financas.application.dto.request;

import com.financas.domain.valueobject.CategoryType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateCategoryRequest {
    @NotBlank
    private String name;

    @NotNull
    private CategoryType type;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String color;

    private String icon;

    private UUID parentId;
}
