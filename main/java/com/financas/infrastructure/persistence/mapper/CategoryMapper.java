package com.financas.infrastructure.persistence.mapper;

import com.financas.domain.model.Category;
import com.financas.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryEntity toEntity(Category c) {
        return CategoryEntity.builder()
                .id(c.getId()).userId(c.getUserId()).name(c.getName())
                .type(c.getType()).color(c.getColor()).icon(c.getIcon())
                .parentId(c.getParentId()).build();
    }

    public Category toDomain(CategoryEntity e) {
        return Category.reconstitute(e.getId(), e.getUserId(), e.getName(),
                e.getType(), e.getColor(), e.getIcon(), e.getParentId());
    }
}
