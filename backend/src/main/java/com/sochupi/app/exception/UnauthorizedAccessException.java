package com.sochupi.app.exception;

/**
 * Thrown when a user tries to access or modify a resource they don't own.
 * Examples: "You don't have permission to modify this budget"
 * Maps to HTTP 403 Forbidden.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
