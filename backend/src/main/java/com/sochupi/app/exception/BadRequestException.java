package com.sochupi.app.exception;

/**
 * Thrown when the request data is technically valid but violates a business rule.
 * Examples: "Transaction date must be within budget month 6/2026"
 * Maps to HTTP 400 Bad Request.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
