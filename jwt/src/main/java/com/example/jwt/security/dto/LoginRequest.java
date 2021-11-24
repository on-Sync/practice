package com.example.jwt.security.dto;

import javax.validation.constraints.Email;

import com.example.jwt.security.annotation.Password;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginRequest {
    @Email
    private String email;
    @Password
    private String password;
}
