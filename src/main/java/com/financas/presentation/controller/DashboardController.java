package com.financas.presentation.controller;

import com.financas.application.dto.response.DashboardResponse;
import com.financas.application.usecase.GetDashboardUseCase;
import com.financas.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final GetDashboardUseCase dashboardUseCase;

    @GetMapping
    @Operation(summary = "Get financial dashboard summary")
    public ResponseEntity<DashboardResponse> get(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int month,
            @RequestParam(defaultValue = "0") int year) {
        LocalDate now = LocalDate.now();
        int m = month == 0 ? now.getMonthValue() : month;
        int y = year == 0 ? now.getYear() : year;
        return ResponseEntity.ok(dashboardUseCase.execute(user.getId(), m, y));
    }
}
