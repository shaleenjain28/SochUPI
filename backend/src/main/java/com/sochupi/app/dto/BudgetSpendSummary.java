package com.sochupi.app.dto;

import java.math.BigDecimal;

/**
 * Snapshot of how much has been spent vs the monthly budget cap.
 * remainingAmount = totalAmount - totalSpent
 */
public record BudgetSpendSummary(
        Long budgetId,
        BigDecimal totalAmount,
        BigDecimal totalSpent,
        BigDecimal remainingAmount,
        BigDecimal savingsTarget
) {}
