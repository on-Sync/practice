package com.example.jwt.security.service;

import java.util.Arrays;

import javax.transaction.Transactional;

import com.example.jwt.security.entity.Role;
import com.example.jwt.security.entity.User;
import com.example.jwt.security.repository.RoleRepository;
import com.example.jwt.security.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserDetailsServiceImpl implements DefaultUserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.role}")
    private String ADMIN_ROLE;
    @Value("${admin.username}")
    private String ADMIN_USERNAME;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            true, // enabled
            true, // accountNonExpired
            true, // credentialsNonExpired  
            true, // accountNonLocked
            user.getRoles() // authorities
        );
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public Role createRoleIfNotFound(String authority) {
        return roleRepository.findByAuthority(authority)
          .orElseGet(()-> roleRepository.save(new Role(authority)));
    }

    @Override
    public User createAdminIfNotFound() {
        Role role = this.createRoleIfNotFound(ADMIN_ROLE);
        return userRepository.findByEmail(ADMIN_USERNAME)
                .orElseGet(() -> 
                    userRepository.save(User.builder()
                        .email(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(Arrays.asList(role))
                        .build()));
    }
}
