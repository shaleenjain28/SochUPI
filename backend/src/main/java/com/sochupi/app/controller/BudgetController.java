package com.sochupi.app.controller;

import com.sochupi.app.dto.request.CreateBudgetRequest;
import com.sochupi.app.dto.response.BudgetResponse;
import com.sochupi.app.service.BudgetService;
import com.sochupi.app.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    // ─────────────────────────────────────────────────────────
    // POST /api/budgets
    // ─────────────────────────────────────────────────────────
    // Notice how we removed @RequestParam Long userId!
    // Now we use @AuthenticationPrincipal to automatically grab the user
    // from the JWT token that was validated by our JwtAuthenticationFilter.
    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateBudgetRequest request) {

        BudgetResponse response = budgetService.createBudget(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────────────────────
    // GET /api/budgets
    // ─────────────────────────────────────────────────────────
    // Instead of putting the ID in the URL (/user/1), the API is now smart
    // enough to just return the budgets for "whoever is holding the token".
    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getMyBudgets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<BudgetResponse> budgets = budgetService.getBudgetsByUser(userDetails.getId());
        return ResponseEntity.ok(budgets);
    }

    // ─────────────────────────────────────────────────────────
    // PATCH /api/budgets/{budgetId}/lock
    // ─────────────────────────────────────────────────────────
    @PatchMapping("/{budgetId}/lock")
    public ResponseEntity<BudgetResponse> toggleLock(
            @PathVariable Long budgetId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        BudgetResponse response = budgetService.toggleLock(budgetId, userDetails.getId());
        return ResponseEntity.ok(response);
    }
}
