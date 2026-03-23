package com.financas.presentation.controller;

import com.financas.application.dto.request.CreateCategoryRequest;
import com.financas.application.dto.response.CategoryResponse;
import com.financas.application.usecase.ManageCategoryUseCase;
import com.financas.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final ManageCategoryUseCase categoryUseCase;

    @PostMapping
    @Operation(summary = "Create category")
    public ResponseEntity<CategoryResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryUseCase.create(user.getId(), request));
    }

    @GetMapping
    @Operation(summary = "List categories")
    public ResponseEntity<List<CategoryResponse>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(categoryUseCase.listByUser(user.getId()));
    }
}
