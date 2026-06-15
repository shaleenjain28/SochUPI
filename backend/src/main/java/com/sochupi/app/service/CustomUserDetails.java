package com.sochupi.app.service;

import com.sochupi.app.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * A wrapper class that adapts our entity User to Spring Security's UserDetails.
 * This keeps our database entity clean from Spring Security-specific logic.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Expose the underlying User entity (useful for getting the ID)
    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // We aren't doing roles (STUDENT, PARENT, ADMIN) yet.
        // For now, return an empty list. Everyone has the same access.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // We authenticate using the email, so email IS the "username"
    }

    // The following methods are for account statuses (banned, locked, expired).
    // We assume all accounts are valid for now.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
