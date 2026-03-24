package com.financas.application.usecase;

import com.financas.application.dto.request.CreateTransactionRequest;
import com.financas.application.dto.response.TransactionResponse;
import com.financas.domain.exception.ResourceNotFoundException;
import com.financas.domain.model.Account;
import com.financas.domain.model.Transaction;
import com.financas.domain.repository.AccountRepository;
import com.financas.domain.repository.TransactionRepository;
import com.financas.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTransactionUseCase {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponse execute(CreateTransactionRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", request.getAccountId()));

        Money amount = Money.of(request.getAmount());

        Transaction transaction = Transaction.create(
                account.getId(),
                account.getUserId(),
                amount,
                request.getType(),
                request.getCategoryId(),
                request.getTransactionDate(),
                request.getDescription()
        );

        if (transaction.isExpense()) {
            account.debit(amount);
        } else {
            account.credit(amount);
        }

        accountRepository.save(account);
        Transaction saved = transactionRepository.save(transaction);

        return toResponse(saved);
    }

    public static TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .accountId(t.getAccountId())
                .categoryId(t.getCategoryId())
                .amount(t.getAmount().amount())
                .type(t.getType())
                .description(t.getDescription())
                .transactionDate(t.getDate())
                .status(t.getStatus())
                .build();
    }
}
