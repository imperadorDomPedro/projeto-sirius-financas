package com.financas.presentation.controller;

import com.financas.application.dto.request.CreateBudgetRequest;
import com.financas.application.dto.response.BudgetResponse;
import com.financas.application.usecase.ManageBudgetUseCase;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Tag(name = "Budgets")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final ManageBudgetUseCase budgetUseCase;

    @PostMapping
    @Operation(summary = "Create budget for category/period")
    public ResponseEntity<BudgetResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateBudgetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetUseCase.create(user.getId(), request));
    }

    @GetMapping
    @Operation(summary = "List budgets by period")
    public ResponseEntity<List<BudgetResponse>> list(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int month,
            @RequestParam(defaultValue = "0") int year) {
        LocalDate now = LocalDate.now();
        int m = month == 0 ? now.getMonthValue() : month;
        int y = year == 0 ? now.getYear() : year;
        return ResponseEntity.ok(budgetUseCase.listByPeriod(user.getId(), m, y));
    }
}
