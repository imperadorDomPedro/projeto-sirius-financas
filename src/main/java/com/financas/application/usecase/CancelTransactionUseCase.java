package com.financas.application.usecase;

import com.financas.domain.exception.ResourceNotFoundException;
import com.financas.domain.model.Account;
import com.financas.domain.model.Transaction;
import com.financas.domain.repository.AccountRepository;
import com.financas.domain.repository.TransactionRepository;
import com.financas.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelTransactionUseCase {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public void execute(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", transactionId));

        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", transaction.getAccountId()));

        Money amount = transaction.getAmount();

        if (transaction.isExpense()) {
            account.credit(amount);
        } else {
            account.debit(amount);
        }

        transaction.cancel();
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }
}
