package com.sochupi.app.exception;

import com.sochupi.app.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * ONE file to handle ALL exceptions from ALL controllers.
 *
 * Without this, Spring returns generic 500 errors with inconsistent JSON formats.
 * With this, every error from our API has the EXACT same structure:
 * {
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "User not found with id: 5",
 *   "path": "/api/users/register",
 *   "timestamp": "2026-06-17T08:00:00"
 * }
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * It catches exceptions thrown by ANY @RestController and converts them to JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── 404 Not Found ───
    // Catches: ResourceNotFoundException
    // When: User/Budget/Transaction not found in the database
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // ─── 409 Conflict ───
    // Catches: DuplicateResourceException
    // When: Trying to register an existing email, or create a budget that already exists
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(
            DuplicateResourceException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // ─── 403 Forbidden ───
    // Catches: UnauthorizedAccessException
    // When: User tries to access someone else's budget or transaction
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(
            UnauthorizedAccessException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    // ─── 400 Bad Request (Business Rule Violation) ───
    // Catches: BadRequestException
    // When: Transaction date doesn't match the budget's month/year
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // ─── 400 Bad Request (Validation Failures) ───
    // Catches: MethodArgumentNotValidException
    // When: @Valid fails on a DTO (e.g., missing @NotNull field, invalid @Email format)
    // This collects ALL validation errors into a single comma-separated message.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    // ─── 401 Unauthorized (Bad Login) ───
    // Catches: BadCredentialsException
    // When: User provides wrong email or password during login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password", request);
    }

    // ─── 500 Internal Server Error (Catch-All) ───
    // Catches: Any exception we haven't explicitly handled above.
    // This is our safety net — if something completely unexpected happens,
    // the user still gets a clean JSON response instead of a stack trace.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong. Please try again later.",
                request
        );
    }

    // ─── HELPER: Build the standardized ErrorResponse ───
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status, String message, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }
}
