package com.sochupi.app.dto;

import com.sochupi.app.entity.TransactionCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long budgetId,
        Long userId,
        BigDecimal amount,
        String description,
        TransactionCategory category,
        LocalDate transactionDate,
        LocalDateTime createdAt
) {}
