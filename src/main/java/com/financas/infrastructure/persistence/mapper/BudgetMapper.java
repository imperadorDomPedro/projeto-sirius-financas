package com.financas.infrastructure.persistence.mapper;

import com.financas.domain.model.Budget;
import com.financas.domain.valueobject.Money;
import com.financas.infrastructure.persistence.entity.BudgetEntity;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {
    public BudgetEntity toEntity(Budget b) {
        return BudgetEntity.builder()
                .id(b.getId()).userId(b.getUserId()).categoryId(b.getCategoryId())
                .limitAmount(b.getLimitAmount().amount())
                .spentAmount(b.getSpentAmount().amount())
                .month(b.getMonth()).year(b.getYear()).build();
    }

    public Budget toDomain(BudgetEntity e) {
        return Budget.reconstitute(e.getId(), e.getUserId(), e.getCategoryId(),
                Money.of(e.getLimitAmount()), Money.of(e.getSpentAmount()),
                e.getMonth(), e.getYear());
    }
}
