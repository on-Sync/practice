package com.example.jwt.security.repository;

import java.util.Optional;

import com.example.jwt.security.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    public Optional<Role> findByAuthority(String authority);
}
