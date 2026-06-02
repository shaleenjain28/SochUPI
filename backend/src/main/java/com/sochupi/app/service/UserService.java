package com.sochupi.app.service;

import com.sochupi.app.dto.RegisterRequest;
import com.sochupi.app.dto.UserResponse;
import com.sochupi.app.entity.User;
import com.sochupi.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    // injecting the repository
    private final UserRepository userRepository;

    public UserResponse registerUser(RegisterRequest req) {

        Optional<User> existingUser = userRepository.findByEmail(req.email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User newUser = new User();
        newUser.setName(req.name());
        newUser.setEmail(req.email());
        // TODO: replace this with real BCrypt hashing later
        newUser.setPasswordHash(req.password());

        User savedUser = userRepository.save(newUser);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getCreatedAt());
    }
}
