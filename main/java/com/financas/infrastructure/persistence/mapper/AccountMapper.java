package com.financas.infrastructure.persistence.mapper;

import com.financas.domain.model.Account;
import com.financas.domain.valueobject.Money;
import com.financas.infrastructure.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountEntity toEntity(Account a) {
        return AccountEntity.builder()
                .id(a.getId()).userId(a.getUserId()).name(a.getName())
                .type(a.getType()).balance(a.getBalance().amount())
                .color(a.getColor()).active(a.isActive()).build();
    }

    public Account toDomain(AccountEntity e) {
        return Account.reconstitute(e.getId(), e.getUserId(), e.getName(),
                e.getType(), Money.of(e.getBalance()), e.getColor(), e.isActive());
    }
}
