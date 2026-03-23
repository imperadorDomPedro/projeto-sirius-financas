package com.financas.infrastructure.persistence.repository;

import com.financas.domain.model.Account;
import com.financas.domain.repository.AccountRepository;
import com.financas.infrastructure.persistence.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final AccountJpaRepository jpa;
    private final AccountMapper mapper;

    @Override
    public Account save(Account a) {
        return mapper.toDomain(jpa.save(mapper.toEntity(a)));
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        return jpa.findByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
