package com.sochupi.app.controller;

import com.sochupi.app.dto.request.RegisterRequest;
import com.sochupi.app.dto.response.UserResponse;
import com.sochupi.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    // 1. Inject the Service
    private final UserService userService;

    // 2. Define the HTTP Endpoint
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {

        // 3. Call the Service
        UserResponse response = userService.registerUser(request);

        // 4. Return the result with a 201 Created status
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
