package com.example.jwt.security.filter;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.jwt.security.dto.LoginRequest;
import com.example.jwt.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class DefaultAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public DefaultAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
            LoginRequest loginRequest = null;
            try {
                loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), 
                loginRequest.getPassword());
            return this.getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authentication) throws IOException, ServletException, EntityNotFoundException {
        String access_token = jwtUtil.create(authentication);
        jwtUtil.issue(response, access_token);
        chain.doFilter(request, response);
    }
}
