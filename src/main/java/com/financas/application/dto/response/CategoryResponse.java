package com.financas.application.dto.response;

import com.financas.domain.valueobject.CategoryType;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data @Builder
public class CategoryResponse {
    private UUID id;

    private String name;

    private CategoryType type;

    private String color;

    private String icon;

    private UUID parentId;
}
