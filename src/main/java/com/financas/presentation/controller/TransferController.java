package com.financas.presentation.controller;

import com.financas.application.dto.request.CreateTransferRequest;
import com.financas.application.dto.response.TransferResponse;
import com.financas.application.usecase.TransferFundsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@Tag(name = "Transfers")
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

    private final TransferFundsUseCase transferUseCase;

    @PostMapping
    @Operation(summary = "Transfer between accounts")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody CreateTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferUseCase.execute(request));
    }
}
