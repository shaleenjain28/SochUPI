package com.sochupi.app.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateBudgetRequest(
    
    @NotNull
    @Min(1) 
    @Max(12) 
    Integer month,
    
    @NotNull
    Integer year,
    
    @NotNull
    @Positive 
    BigDecimal totalAmount,
    
    @NotNull
    @PositiveOrZero 
    BigDecimal savingsTarget,
    
    @NotNull
    Boolean isLocked
) {}
