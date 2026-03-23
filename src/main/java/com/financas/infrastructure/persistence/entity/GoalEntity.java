package com.financas.infrastructure.persistence.entity;

import com.financas.domain.valueobject.GoalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "goals")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Column(name = "target_amount", precision = 15, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "current_amount", precision = 15, scale = 2)
    private BigDecimal currentAmount;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private GoalStatus status;
}
