package com.financas.presentation.controller;

import com.financas.application.dto.request.CreateGoalRequest;
import com.financas.application.dto.request.GoalContributeRequest;
import com.financas.application.dto.response.GoalResponse;
import com.financas.application.usecase.ManageGoalUseCase;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Goals")
@SecurityRequirement(name = "bearerAuth")
public class GoalController {

    private final ManageGoalUseCase goalUseCase;

    @PostMapping
    @Operation(summary = "Create financial goal")
    public ResponseEntity<GoalResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateGoalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goalUseCase.create(user.getId(), request));
    }

    @GetMapping
    @Operation(summary = "List goals")
    public ResponseEntity<List<GoalResponse>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(goalUseCase.listByUser(user.getId()));
    }

    @PostMapping("/{id}/contribute")
    @Operation(summary = "Contribute to a goal")
    public ResponseEntity<GoalResponse> contribute(
            @PathVariable UUID id,
            @Valid @RequestBody GoalContributeRequest request) {
        return ResponseEntity.ok(goalUseCase.contribute(id, request));
    }
}
