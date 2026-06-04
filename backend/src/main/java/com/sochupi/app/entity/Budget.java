package com.sochupi.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship: Many Budgets can belong to One User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer month; // e.g., 6 for June

    @Column(nullable = false)
    private Integer year; // e.g., 2026

    // precision = 10 (total digits), scale = 2 (decimal places) -> e.g., 99999999.99
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "savings_target", nullable = false, precision = 10, scale = 2)
    private BigDecimal savingsTarget;

    // We set a default value of false so when it's created, it's open for editing
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
