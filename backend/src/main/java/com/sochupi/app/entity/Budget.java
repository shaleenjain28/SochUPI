package com.sochupi.app.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
// No @Data — this entity has @ManyToOne User. @Data's toString/equals/hashCode include `user`,
// which can lazy-load User from a log line or debugger hover. Identity = id only.
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include              // safe to log — won't fetch linked User
    @EqualsAndHashCode.Include
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
