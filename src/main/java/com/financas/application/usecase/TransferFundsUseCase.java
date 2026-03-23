package com.financas.application.usecase;

import com.financas.application.dto.request.CreateTransferRequest;
import com.financas.application.dto.response.TransferResponse;
import com.financas.domain.exception.BusinessException;
import com.financas.domain.exception.ResourceNotFoundException;
import com.financas.domain.model.Account;
import com.financas.domain.repository.AccountRepository;
import com.financas.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferFundsUseCase {

    private final AccountRepository accountRepository;

    @Transactional
    public TransferResponse execute(CreateTransferRequest request) {
        if (request.getOriginAccountId().equals(request.getDestAccountId())) {
            throw new BusinessException("Origin and destination accounts must be different");
        }

        Account origin = accountRepository.findById(request.getOriginAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", request.getOriginAccountId()));

        Account destination = accountRepository.findById(request.getDestAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", request.getDestAccountId()));

        Money amount = Money.of(request.getAmount());
        origin.debit(amount);
        destination.credit(amount);

        accountRepository.save(origin);
        accountRepository.save(destination);

        return TransferResponse.builder()
                .id(UUID.randomUUID())
                .originAccountId(origin.getId())
                .destAccountId(destination.getId())
                .amount(amount.amount())
                .transferDate(request.getTransferDate())
                .description(request.getDescription())
                .build();
    }
}
