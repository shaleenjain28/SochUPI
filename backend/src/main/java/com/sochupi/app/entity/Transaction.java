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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
// ─── FIX #1: No @Data on JPA entities with @ManyToOne relationships ───
// Transaction has TWO @ManyToOne links (user + budget).
// @Data's toString() would try to print the User and Budget objects,
// triggering a lazy-load from the DB even when we don't need them.
// @Data's equals()/hashCode() would include all fields — if `user` hasn't
// been loaded yet, calling equals() would fire a SELECT query silently.
// Instead: @Getter/@Setter are safe, and we manually control which fields
// appear in toString() and equals()/hashCode().
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include              // Only the id shows up in logs/debugger
    @EqualsAndHashCode.Include     // Two Transaction objects are "equal" if same id
    private Long id;

    // ─── Relationship 1: Who made this transaction? ───
    // Many transactions can belong to ONE user.
    // @JoinColumn tells Hibernate: "create a column 'user_id' as a foreign key"
    // NOT included in toString/equals — avoids lazy-load traps
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ─── Relationship 2: Which budget does this count against? ───
    // Many transactions fall under ONE monthly budget.
    // NOT included in toString/equals — same reason
    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    // ─── How much was spent? ───
    // BigDecimal for money, always. precision=10, scale=2.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // ─── What was it for? ───
    // e.g., "Swiggy order", "Auto fare", "Library book"
    @Column(nullable = false, length = 255)
    private String description;

    // ─── FIX #2: Changed from String → TransactionCategory enum ───
    // The DTO, Repository, and Service all use TransactionCategory.
    // The entity MUST match — otherwise setCategory(req.category()) won't compile.
    //
    // @Enumerated(EnumType.STRING) tells Hibernate:
    //   "Store this enum as its NAME in the database" → column value = "FOOD", "TRAVEL", etc.
    //   Without this, Hibernate defaults to EnumType.ORDINAL (stores 0, 1, 2...)
    //   which BREAKS if you reorder or add new enum values later.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TransactionCategory category;

    // ─── When did the spending ACTUALLY happen? ───
    // LocalDate (not LocalDateTime) — we only care about the date.
    // Different from createdAt: you might log yesterday's expense today.
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    // ─── Auto-managed timestamps ───
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
