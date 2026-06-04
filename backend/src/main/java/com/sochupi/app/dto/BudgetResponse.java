package com.sochupi.app.dto;

import java.math.BigDecimal;

public record BudgetResponse(
    Long id,
    Long userId,
    Integer month,
    Integer year,
    BigDecimal totalAmount,
    BigDecimal savingsTarget,
    Boolean isLocked
) {}
