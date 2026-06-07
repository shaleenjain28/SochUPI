package com.sochupi.app.repository;

import com.sochupi.app.entity.Transaction;
import com.sochupi.app.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // All transactions for a user (history screen)
    List<Transaction> findByUser_IdOrderByTransactionDateDesc(Long userId);

    // All transactions linked to a specific monthly budget
    List<Transaction> findByBudget_IdOrderByTransactionDateDesc(Long budgetId);

    // Filter by category — e.g. "show me all Food spends this month"
    List<Transaction> findByUser_IdAndCategoryOrderByTransactionDateDesc(
            Long userId, TransactionCategory category);

    // Date-range filter — useful for weekly/monthly reports
    List<Transaction> findByUser_IdAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long userId, LocalDate from, LocalDate to);

    // Sum all spending for a budget — powers "remaining balance" calculation
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budget.id = :budgetId")
    BigDecimal sumAmountByBudgetId(@Param("budgetId") Long budgetId);

    // Per-category spend within a budget — for category limit nudges later
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.budget.id = :budgetId AND t.category = :category")
    BigDecimal sumAmountByBudgetIdAndCategory(
            @Param("budgetId") Long budgetId,
            @Param("category") TransactionCategory category);
}
