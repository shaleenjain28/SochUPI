package com.sochupi.app.exception;

/**
 * Thrown when we try to find something in the database and it doesn't exist.
 * Examples: "User not found with id: 5", "Budget not found with id: 10"
 * Maps to HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
