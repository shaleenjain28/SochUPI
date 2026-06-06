package com.sochupi.app.controller;

import com.sochupi.app.dto.BudgetSpendSummary;
import com.sochupi.app.dto.CreateTransactionRequest;
import com.sochupi.app.dto.TransactionResponse;
import com.sochupi.app.entity.TransactionCategory;
import com.sochupi.app.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // POST /api/transactions?userId=1
    @PostMapping
    public ResponseEntity<TransactionResponse> addTransaction(
            @RequestParam Long userId,
            @Valid @RequestBody CreateTransactionRequest request) {

        TransactionResponse response = transactionService.addTransaction(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/transactions/user/{userId}
    // Optional filters: ?budgetId=1&category=FOOD&from=2026-06-01&to=2026-06-30
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false) Long budgetId,
            @RequestParam(required = false) TransactionCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<TransactionResponse> transactions = transactionService.getTransactions(
                userId, budgetId, category, from, to);
        return ResponseEntity.ok(transactions);
    }

    // GET /api/transactions/budget/{budgetId}/summary?userId=1
    @GetMapping("/budget/{budgetId}/summary")
    public ResponseEntity<BudgetSpendSummary> getBudgetSpendSummary(
            @PathVariable Long budgetId,
            @RequestParam Long userId) {

        BudgetSpendSummary summary = transactionService.getBudgetSpendSummary(budgetId, userId);
        return ResponseEntity.ok(summary);
    }
}
