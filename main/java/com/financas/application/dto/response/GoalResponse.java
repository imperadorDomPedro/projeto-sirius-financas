package com.financas.application.dto.response;

import com.financas.domain.valueobject.GoalStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data @Builder
public class GoalResponse {
    private UUID id;

    private String name;

    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    private double progressPercent;

    private LocalDate deadline;

    private GoalStatus status;
}
