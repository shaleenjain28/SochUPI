package com.sochupi.app.controller;

import com.sochupi.app.dto.BudgetResponse;
import com.sochupi.app.dto.CreateBudgetRequest;
import com.sochupi.app.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")    // All endpoints in this controller start with /api/budgets
@RequiredArgsConstructor
public class BudgetController {

    // Inject the service — controller NEVER talks to repository directly
    private final BudgetService budgetService;

    // ─────────────────────────────────────────────────────────
    // POST /api/budgets?userId=1
    // ─────────────────────────────────────────────────────────
    // Why @RequestParam for userId?
    // Because later when we add JWT, the userId will come from the token.
    // Using @RequestParam now makes it easy to swap out later without
    // changing the request body structure.
    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @RequestParam Long userId,
            @Valid @RequestBody CreateBudgetRequest request) {

        BudgetResponse response = budgetService.createBudget(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────────────────────
    // GET /api/budgets/user/{userId}
    // ─────────────────────────────────────────────────────────
    // @PathVariable extracts the value from the URL itself
    // e.g., GET /api/budgets/user/5 → userId = 5
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetResponse>> getBudgetsByUser(
            @PathVariable Long userId) {

        List<BudgetResponse> budgets = budgetService.getBudgetsByUser(userId);
        return ResponseEntity.ok(budgets);  // 200 OK
    }

    // ─────────────────────────────────────────────────────────
    // PATCH /api/budgets/{budgetId}/lock?userId=1
    // ─────────────────────────────────────────────────────────
    // Why PATCH and not PUT?
    // PUT = replace the entire resource
    // PATCH = modify a single field (isLocked in our case)
    // Semantically, PATCH is the correct HTTP verb here.
    @PatchMapping("/{budgetId}/lock")
    public ResponseEntity<BudgetResponse> toggleLock(
            @PathVariable Long budgetId,
            @RequestParam Long userId) {

        BudgetResponse response = budgetService.toggleLock(budgetId, userId);
        return ResponseEntity.ok(response);
    }
}
