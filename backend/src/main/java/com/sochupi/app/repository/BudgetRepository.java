package com.sochupi.app.repository;

import com.sochupi.app.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Spring Data JPA reads the method name and auto-generates the SQL for us!
    // "findBy" + "UserId" + "And" + "Month" + "And" + "Year"
    // → SELECT * FROM budgets WHERE user_id = ? AND month = ? AND year = ?
    // Used to enforce: one budget per user per month
    Optional<Budget> findByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);

    // SELECT * FROM budgets WHERE user_id = ?
    // Used to show a user their full budget history
    List<Budget> findByUserId(Long userId);

    // SELECT * FROM budgets WHERE user_id = ? AND year = ?
    // Used to show budgets for a specific year (e.g., all months of 2026)
    List<Budget> findByUserIdAndYear(Long userId, Integer year);
}
