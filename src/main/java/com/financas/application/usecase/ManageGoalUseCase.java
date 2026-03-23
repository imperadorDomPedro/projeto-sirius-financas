package com.financas.application.usecase;

import com.financas.application.dto.request.CreateGoalRequest;
import com.financas.application.dto.request.GoalContributeRequest;
import com.financas.application.dto.response.GoalResponse;
import com.financas.domain.exception.ResourceNotFoundException;
import com.financas.domain.model.Goal;
import com.financas.domain.repository.GoalRepository;
import com.financas.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManageGoalUseCase {

    private final GoalRepository goalRepository;

    @Transactional
    public GoalResponse create(UUID userId, CreateGoalRequest request) {
        Goal goal = Goal.create(userId, request.getName(),
                Money.of(request.getTargetAmount()), request.getDeadline());
        return toResponse(goalRepository.save(goal));
    }

    @Transactional
    public GoalResponse contribute(UUID goalId, GoalContributeRequest request) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", goalId));
        goal.contribute(Money.of(request.getAmount()));
        return toResponse(goalRepository.save(goal));
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> listByUser(UUID userId) {
        return goalRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public GoalResponse toResponse(Goal g) {
        return GoalResponse.builder()
                .id(g.getId())
                .name(g.getName())
                .targetAmount(g.getTargetAmount().amount())
                .currentAmount(g.getCurrentAmount().amount())
                .progressPercent(g.getProgressPercent())
                .deadline(g.getDeadline())
                .status(g.getStatus())
                .build();
    }
}
