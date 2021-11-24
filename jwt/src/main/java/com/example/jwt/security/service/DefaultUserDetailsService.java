package com.example.jwt.security.service;

import com.example.jwt.security.entity.Role;
import com.example.jwt.security.entity.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface DefaultUserDetailsService extends UserDetailsService {
    public User save(User entity);

    public Role createRoleIfNotFound(String string);
    public User createAdminIfNotFound();
}
