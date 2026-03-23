package com.financas.domain.repository;

import com.financas.domain.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(UUID id);
    List<Account> findByUserId(UUID userId);
    void deleteById(UUID id);
}
