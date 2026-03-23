package com.financas.infrastructure.persistence.mapper;

import com.financas.domain.model.Goal;
import com.financas.domain.valueobject.Money;
import com.financas.infrastructure.persistence.entity.GoalEntity;
import org.springframework.stereotype.Component;

@Component
public class GoalMapper {
    public GoalEntity toEntity(Goal g) {
        return GoalEntity.builder()
                .id(g.getId()).userId(g.getUserId()).name(g.getName())
                .targetAmount(g.getTargetAmount().amount())
                .currentAmount(g.getCurrentAmount().amount())
                .deadline(g.getDeadline()).status(g.getStatus()).build();
    }

    public Goal toDomain(GoalEntity e) {
        return Goal.reconstitute(e.getId(), e.getUserId(), e.getName(),
                Money.of(e.getTargetAmount()), Money.of(e.getCurrentAmount()),
                e.getDeadline(), e.getStatus());
    }
}
