package com.sochupi.app.controller;

import com.sochupi.app.dto.request.LoginRequest;
import com.sochupi.app.dto.response.AuthResponse;
import com.sochupi.app.service.CustomUserDetailsService;
import com.sochupi.app.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        
        // 1. Verify the email and password against the database.
        // Spring Security's AuthenticationManager does the BCrypt password matching for us.
        // If the password is wrong, this throws an exception and immediately returns a 401 Unauthorized.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // 2. If we reach here, the credentials are correct! Load the user details.
        UserDetails user = userDetailsService.loadUserByUsername(request.email());

        // 3. Generate a JWT token for the authenticated user
        String token = jwtService.generateToken(user);

        // 4. Return the token to the client
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
