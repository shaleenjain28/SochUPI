package com.sochupi.app.dto;

import com.sochupi.app.entity.TransactionCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(

        @NotNull
        Long budgetId,

        @NotNull
        @Positive
        BigDecimal amount,

        String description,

        @NotNull
        TransactionCategory category,

        @NotNull
        LocalDate transactionDate
) {}
