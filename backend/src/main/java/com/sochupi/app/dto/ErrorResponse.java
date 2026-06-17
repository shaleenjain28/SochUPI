package com.sochupi.app.dto;

import java.time.LocalDateTime;

/**
 * Standardized error response returned by ALL error endpoints.
 * Every error from our API looks exactly the same — consistent contract for the mobile app.
 */
public record ErrorResponse(
        int status,              // HTTP status code (e.g., 404)
        String error,            // Short label (e.g., "Not Found")
        String message,          // Human-readable explanation (e.g., "User not found with id: 5")
        String path,             // The endpoint that was hit (e.g., "/api/budgets")
        LocalDateTime timestamp  // When the error happened
) {}
