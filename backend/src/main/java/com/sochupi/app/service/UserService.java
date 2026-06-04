package com.sochupi.app.service;

import com.sochupi.app.dto.RegisterRequest;
import com.sochupi.app.dto.UserResponse;
import com.sochupi.app.entity.User;
import com.sochupi.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor

public class UserService {
    // injecting the repository
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterRequest req) {

        // 1. Check if email exists
        Optional<User> existingUser = userRepository.findByEmail(req.email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        // 2. Create the entity
        User newUser = new User();
        newUser.setName(req.name());
        newUser.setEmail(req.email());

        // TODO: We will replace this with real BCrypt hashing later!
        String passwordHash = passwordEncoder.encode(req.password());
        newUser.setPasswordHash(passwordHash);

        // 3. Save to database (capture the returned object!)
        User savedUser = userRepository.save(newUser);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getCreatedAt());
    }
}
