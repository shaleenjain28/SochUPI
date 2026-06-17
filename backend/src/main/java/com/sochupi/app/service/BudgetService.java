package com.sochupi.app.service;

import com.sochupi.app.dto.request.CreateBudgetRequest;
import com.sochupi.app.dto.response.BudgetResponse;
import com.sochupi.app.entity.Budget;
import com.sochupi.app.entity.User;
import com.sochupi.app.exception.DuplicateResourceException;
import com.sochupi.app.exception.ResourceNotFoundException;
import com.sochupi.app.exception.UnauthorizedAccessException;
import com.sochupi.app.repository.BudgetRepository;
import com.sochupi.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok generates a constructor injecting all 'final' fields
public class BudgetService {

    // We need BOTH repositories:
    // - UserRepository → to verify the user actually exists before creating a budget
    // - BudgetRepository → to do the actual budget CRUD operations
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;

    // ─────────────────────────────────────────────────────────
    // 1. CREATE BUDGET
    // ─────────────────────────────────────────────────────────
    // Why does this method take userId separately?
    // Because the userId will eventually come from the JWT token (authenticated user),
    // NOT from the request body. For now we pass it explicitly.
    public BudgetResponse createBudget(Long userId, CreateBudgetRequest req) {

        // Step 1: Does this user even exist?
        // .orElseThrow() → if the Optional is empty, throw an exception immediately
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Step 2: One budget per user per month — enforce the rule!
        // We use the custom query method we just wrote in BudgetRepository
        Optional<Budget> existingBudget = budgetRepository
                .findByUserIdAndMonthAndYear(userId, req.month(), req.year());

        if (existingBudget.isPresent()) {
            throw new DuplicateResourceException(
                    "Budget already exists for " + req.month() + "/" + req.year()
            );
        }

        // Step 3: Map the DTO → Entity
        // The request (DTO) is what the mobile app sends.
        // The entity is what Hibernate saves to the database.
        // We manually map between them — this keeps our layers clean.
        Budget budget = new Budget();
        budget.setUser(user);                       // Link this budget to the user
        budget.setMonth(req.month());
        budget.setYear(req.year());
        budget.setTotalAmount(req.totalAmount());
        budget.setSavingsTarget(req.savingsTarget());
        budget.setIsLocked(req.isLocked());         // User decides: lock now or later

        // Step 4: Save to DB
        // .save() returns the saved entity WITH the auto-generated id and timestamps
        Budget savedBudget = budgetRepository.save(budget);

        // Step 5: Map Entity → Response DTO
        // We never send the raw entity back — always use a DTO
        // This gives us control over exactly what the client sees
        return mapToResponse(savedBudget);
    }

    // ─────────────────────────────────────────────────────────
    // 2. GET ALL BUDGETS FOR A USER
    // ─────────────────────────────────────────────────────────
    // Used for the budget history / list screen in the app
    public List<BudgetResponse> getBudgetsByUser(Long userId) {

        // First verify the user exists (good practice — fail fast with a clear message)
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Fetch all budgets, then convert each entity → DTO using our helper method
        // .stream() → turns the list into a pipeline we can transform
        // .map() → applies mapToResponse to each item
        // .collect() → gathers the results back into a List
        return budgetRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────
    // 3. TOGGLE LOCK
    // ─────────────────────────────────────────────────────────
    // Why toggle instead of "setLocked(true)"?
    // Because the user might want to unlock if they made a mistake
    // (as long as it's the same month — we can add time checks later)
    public BudgetResponse toggleLock(Long budgetId, Long userId) {

        // Fetch the budget
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + budgetId));

        // Security check: does this budget belong to this user?
        // Without this, any user could lock/unlock anyone else's budget!
        if (!budget.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You don't have permission to modify this budget");
        }

        // Flip the boolean: true → false, false → true
        budget.setIsLocked(!budget.getIsLocked());

        // Save the updated entity
        Budget updatedBudget = budgetRepository.save(budget);

        return mapToResponse(updatedBudget);
    }

    // ─────────────────────────────────────────────────────────
    // HELPER: Entity → Response DTO mapping
    // ─────────────────────────────────────────────────────────
    // Why a separate method? Because we do this conversion in EVERY method above.
    // DRY principle: Don't Repeat Yourself.
    private BudgetResponse mapToResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getUser().getId(),   // We send the userId, NOT the whole User object
                budget.getMonth(),
                budget.getYear(),
                budget.getTotalAmount(),
                budget.getSavingsTarget(),
                budget.getIsLocked()
        );
    }
}
