package com.sochupi.app.service;

import com.sochupi.app.entity.User;
import com.sochupi.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * This method is called by Spring Security when a user tries to log in, 
     * or when we parse a JWT token and need to verify the user exists.
     * 
     * @param email The email extracted from the login request or JWT token.
     * @return UserDetails wrapper containing the found user.
     * @throws UsernameNotFoundException if the email is not in the DB.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return new CustomUserDetails(user);
    }
}
