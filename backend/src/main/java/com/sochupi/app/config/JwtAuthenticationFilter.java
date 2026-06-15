package com.sochupi.app.config;

import com.sochupi.app.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Intercepts every single HTTP request to our API.
 * It checks for the "Authorization" header, extracts the JWT token, 
 * validates it, and tells Spring Security "Yes, this user is authenticated".
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Check if the request has an Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // If there's no header, or it doesn't start with "Bearer ", 
        // we can't do anything. Pass it to the next filter (Spring will likely block it).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; 
        }

        // 2. Extract the token (Remove "Bearer " which is 7 characters)
        jwt = authHeader.substring(7);

        try {
            // 3. Extract the email from the token payload
            userEmail = jwtService.extractUsername(jwt);
            
            // 4. If we found an email AND the user isn't already logged in for this request...
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Fetch the user from the database
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                // 5. Verify the token signature and expiration against the user
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    
                    // Create an authentication object
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // We don't need the password because the token is proof
                            userDetails.getAuthorities()
                    );
                    
                    // Add details like the user's IP address
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // 6. Put the authentication object into the Security Context!
                    // This is how Spring knows the user is officially "logged in" for this request.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // If the token is expired, malformed, or tampered with, JwtService throws an exception.
            // We catch it and do nothing — SecurityContext remains null, and Spring blocks the request.
            System.out.println("JWT Validation Failed: " + e.getMessage());
        }

        // 7. Always continue the filter chain
        filterChain.doFilter(request, response);
    }
}
