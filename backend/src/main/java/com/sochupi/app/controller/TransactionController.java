package com.sochupi.app.controller;

import com.sochupi.app.dto.request.CreateTransactionRequest;
import com.sochupi.app.dto.response.BudgetSpendSummary;
import com.sochupi.app.dto.response.TransactionResponse;
import com.sochupi.app.entity.TransactionCategory;
import com.sochupi.app.service.CustomUserDetails;
import com.sochupi.app.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // ─────────────────────────────────────────────────────────
    // 1. ADD TRANSACTION
    // ─────────────────────────────────────────────────────────
    // POST /api/transactions
    @PostMapping
    public ResponseEntity<TransactionResponse> addTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateTransactionRequest request) {

        TransactionResponse response = transactionService.addTransaction(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────────────────────
    // 2. LIST TRANSACTIONS (with optional filters)
    // ─────────────────────────────────────────────────────────
    // GET /api/transactions?budgetId=1&category=FOOD
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getMyTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long budgetId,
            @RequestParam(required = false) TransactionCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<TransactionResponse> transactions = transactionService.getTransactions(
                userDetails.getId(), budgetId, category, from, to);
        return ResponseEntity.ok(transactions);
    }

    // ─────────────────────────────────────────────────────────
    // 3. BUDGET SPEND SUMMARY
    // ─────────────────────────────────────────────────────────
    // GET /api/transactions/budget/{budgetId}/summary
    @GetMapping("/budget/{budgetId}/summary")
    public ResponseEntity<BudgetSpendSummary> getBudgetSpendSummary(
            @PathVariable Long budgetId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        BudgetSpendSummary summary = transactionService.getBudgetSpendSummary(budgetId, userDetails.getId());
        return ResponseEntity.ok(summary);
    }
}
