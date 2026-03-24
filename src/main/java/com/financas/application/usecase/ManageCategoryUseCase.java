package com.financas.application.usecase;

import com.financas.application.dto.request.CreateCategoryRequest;
import com.financas.application.dto.response.CategoryResponse;
import com.financas.domain.model.Category;
import com.financas.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManageCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse create(UUID userId, CreateCategoryRequest request) {
        Category category = Category.create(userId, request.getName(),
                request.getType(), request.getColor(), request.getIcon(), request.getParentId());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> listByUser(UUID userId) {
        return categoryRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId()).name(c.getName()).type(c.getType())
                .color(c.getColor()).icon(c.getIcon()).parentId(c.getParentId()).build();
    }
}
