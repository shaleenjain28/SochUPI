package com.sochupi.app.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
// No @Data — budget & user are LAZY. @Data's toString()/hashCode() touch ALL fields, so even
// log.info("{}", tx) or hovering in the debugger fires extra SQL to load Budget + User (N+1).
// Only call getBudget()/getUser() when you deliberately need them (e.g. mapToResponse).
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include              // log shows "Transaction(id=42)" — no hidden SQL
    @EqualsAndHashCode.Include     // HashSet/add won't lazy-load budget or user
    private Long id;

    // LAZY: Hibernate loads Budget only when getBudget() is called — not on toString/hashCode
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    // LAZY: same rule — avoid accidental access via logging or collections
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 255)
    private String description;

    // EnumType.STRING → saves "FOOD" not 0 (ordinal would break if we reorder the enum)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionCategory category;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
