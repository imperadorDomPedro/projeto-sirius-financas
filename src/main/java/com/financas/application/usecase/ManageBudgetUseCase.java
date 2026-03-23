package com.financas.application.usecase;

import com.financas.application.dto.request.CreateBudgetRequest;
import com.financas.application.dto.response.BudgetResponse;
import com.financas.domain.exception.BusinessException;
import com.financas.domain.exception.ResourceNotFoundException;
import com.financas.domain.model.Budget;
import com.financas.domain.repository.BudgetRepository;
import com.financas.domain.repository.CategoryRepository;
import com.financas.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManageBudgetUseCase {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BudgetResponse create(UUID userId, CreateBudgetRequest request) {
        categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
                userId, request.getCategoryId(), request.getMonth(), request.getYear())
                .ifPresent(b -> { throw new BusinessException("Budget already exists for this category/period"); });

        Budget budget = Budget.create(userId, request.getCategoryId(),
                Money.of(request.getLimitAmount()), request.getMonth(), request.getYear());

        return toResponse(budgetRepository.save(budget), null);
    }

    @Transactional(readOnly = true)
    public List<BudgetResponse> listByPeriod(UUID userId, int month, int year) {
        return budgetRepository.findByUserIdAndMonthAndYear(userId, month, year)
                .stream().map(b -> toResponse(b, null)).toList();
    }

    public BudgetResponse toResponse(Budget b, String categoryName) {
        return BudgetResponse.builder()
                .id(b.getId())
                .categoryId(b.getCategoryId())
                .categoryName(categoryName)
                .limitAmount(b.getLimitAmount().amount())
                .spentAmount(b.getSpentAmount().amount())
                .remainingAmount(b.getRemainingAmount().amount())
                .usagePercent(b.getUsagePercent())
                .exceeded(b.isExceeded())
                .month(b.getMonth())
                .year(b.getYear())
                .build();
    }
}
