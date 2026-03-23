package com.financas.application.usecase;

import com.financas.application.dto.request.CreateAccountRequest;
import com.financas.application.dto.response.AccountResponse;
import com.financas.domain.model.Account;
import com.financas.domain.repository.AccountRepository;
import com.financas.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateAccountUseCase {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponse execute(UUID userId, CreateAccountRequest request) {
        Account account = Account.create(
                userId,
                request.getName(),
                request.getType(),
                Money.of(request.getInitialBalance()),
                request.getColor()
        );
        return toResponse(accountRepository.save(account));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listByUser(UUID userId) {
        return accountRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public AccountResponse toResponse(Account a) {
        return AccountResponse.builder()
                .id(a.getId())
                .name(a.getName())
                .type(a.getType())
                .balance(a.getBalance().amount())
                .color(a.getColor())
                .active(a.isActive())
                .build();
    }
}
