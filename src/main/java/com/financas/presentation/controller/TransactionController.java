package com.financas.presentation.controller;

import com.financas.application.dto.request.CreateTransactionRequest;
import com.financas.application.dto.response.TransactionResponse;
import com.financas.application.usecase.CancelTransactionUseCase;
import com.financas.application.usecase.CreateTransactionUseCase;
import com.financas.application.usecase.GetTransactionsUseCase;
import com.financas.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final CreateTransactionUseCase createUseCase;
    private final GetTransactionsUseCase getUseCase;
    private final CancelTransactionUseCase cancelUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create transaction")
    public ResponseEntity<TransactionResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createUseCase.execute(request));
    }

    @GetMapping
    @Operation(summary = "List transactions by period")
    public ResponseEntity<List<TransactionResponse>> list(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(getUseCase.execute(user.getId(), from, to));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel transaction")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        cancelUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
