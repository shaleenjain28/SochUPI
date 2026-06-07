package com.sochupi.app.entity;

/**
 * Fixed spending categories for SochUPI.
 * Stored as STRING in MySQL (e.g. "FOOD") so the DB stays human-readable.
 */
public enum TransactionCategory {
    FOOD,
    TRAVEL,
    BOOKS,
    MEDICAL,
    EMERGENCY_FUND,
    SHOPPING,
    ENTERTAINMENT,
    OTHER
}
