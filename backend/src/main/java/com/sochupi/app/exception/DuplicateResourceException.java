package com.sochupi.app.exception;

/**
 * Thrown when the user tries to create something that already exists.
 * Examples: "User already exists", "Budget already exists for 6/2026"
 * Maps to HTTP 409 Conflict.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
