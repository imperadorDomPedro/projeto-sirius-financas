package com.financas.presentation.controller;

import com.financas.application.dto.request.CreateAccountRequest;
import com.financas.application.dto.response.AccountResponse;
import com.financas.application.usecase.CreateAccountUseCase;
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
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final CreateAccountUseCase accountUseCase;

    @PostMapping
    @Operation(summary = "Create account")
    public ResponseEntity<AccountResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountUseCase.execute(user.getId(), request));
    }

    @GetMapping
    @Operation(summary = "List user accounts")
    public ResponseEntity<List<AccountResponse>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountUseCase.listByUser(user.getId()));
    }
}
