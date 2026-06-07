package com.sochupi.app.service;

import com.sochupi.app.dto.BudgetSpendSummary;
import com.sochupi.app.dto.CreateTransactionRequest;
import com.sochupi.app.dto.TransactionResponse;
import com.sochupi.app.entity.Budget;
import com.sochupi.app.entity.Transaction;
import com.sochupi.app.entity.TransactionCategory;
import com.sochupi.app.entity.User;
import com.sochupi.app.repository.BudgetRepository;
import com.sochupi.app.repository.TransactionRepository;
import com.sochupi.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    // ─────────────────────────────────────────────────────────
    // 1. ADD TRANSACTION
    // ─────────────────────────────────────────────────────────
    public TransactionResponse addTransaction(Long userId, CreateTransactionRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Budget budget = budgetRepository.findById(req.budgetId())
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + req.budgetId()));

        // Security: a user can only log spending against their own budget
        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to use this budget");
        }

        // Domain rule: transaction must fall in the budget's month/year
        LocalDate date = req.transactionDate();
        if (date.getMonthValue() != budget.getMonth() || date.getYear() != budget.getYear()) {
            throw new RuntimeException(
                    "Transaction date must be within budget month " + budget.getMonth() + "/" + budget.getYear()
            );
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setBudget(budget);
        transaction.setAmount(req.amount());
        transaction.setDescription(req.description());
        transaction.setCategory(req.category());
        transaction.setTransactionDate(date);

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    // ─────────────────────────────────────────────────────────
    // 2. LIST TRANSACTIONS (with optional filters)
    // ─────────────────────────────────────────────────────────
    public List<TransactionResponse> getTransactions(
            Long userId,
            Long budgetId,
            TransactionCategory category,
            LocalDate from,
            LocalDate to) {

        verifyUserExists(userId);

        List<Transaction> transactions;

        if (budgetId != null) {
            verifyBudgetOwnership(budgetId, userId);
            transactions = transactionRepository.findByBudget_IdOrderByTransactionDateDesc(budgetId);
        } else if (category != null && from != null && to != null) {
            transactions = filterByDateRange(
                    transactionRepository.findByUser_IdAndCategoryOrderByTransactionDateDesc(userId, category),
                    from, to);
        } else if (category != null) {
            transactions = transactionRepository.findByUser_IdAndCategoryOrderByTransactionDateDesc(userId, category);
        } else if (from != null && to != null) {
            transactions = transactionRepository.findByUser_IdAndTransactionDateBetweenOrderByTransactionDateDesc(
                    userId, from, to);
        } else {
            transactions = transactionRepository.findByUser_IdOrderByTransactionDateDesc(userId);
        }

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────
    // 3. BUDGET SPEND SUMMARY (remaining balance)
    // ─────────────────────────────────────────────────────────
    public BudgetSpendSummary getBudgetSpendSummary(Long budgetId, Long userId) {

        Budget budget = verifyBudgetOwnership(budgetId, userId);

        BigDecimal totalSpent = transactionRepository.sumAmountByBudgetId(budgetId);
        BigDecimal remaining = budget.getTotalAmount().subtract(totalSpent);

        return new BudgetSpendSummary(
                budget.getId(),
                budget.getTotalAmount(),
                totalSpent,
                remaining,
                budget.getSavingsTarget()
        );
    }

    // ─────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────
    private void verifyUserExists(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private Budget verifyBudgetOwnership(Long budgetId, Long userId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + budgetId));

        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to access this budget");
        }
        return budget;
    }

    private List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(t -> !t.getTransactionDate().isBefore(from) && !t.getTransactionDate().isAfter(to))
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getBudget().getId(),
                transaction.getUser().getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCategory(),
                transaction.getTransactionDate(),
                transaction.getCreatedAt()
        );
    }
}
