package com.example.jwt.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.jwt.security.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

	private static final AntPathRequestMatcher DEFAULT_LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isRequestForJwtAuthentication(request)) {
            String token = jwtUtil.resolve(request);
            Authentication authentication = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request , response);
    }

	private boolean isRequestForJwtAuthentication(HttpServletRequest request) {
        return ! DEFAULT_LOGIN_REQUEST_MATCHER.matches(request);
	}

}