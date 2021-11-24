package com.example.jwt.security.util;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    
    @Autowired

    private final static String AUTHORIZATION = "AUTHORIZATION";
    private final static String BEARER = "Bearer";

    @Value("${signature.token.secret}")
    private String TOKEN_SECRET;
    @Value("${signature.token.expiration}")
    private String TOKEN_EXPIRATION;

    private Jws<Claims> parse(String token) {
        return Jwts.parser()
            .setSigningKey(TOKEN_SECRET)
            .parseClaimsJws(token);
            // ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException
    }

    public String create(Authentication authentication) {
        String email = ((User)authentication.getPrincipal()).getUsername();
        Map<String, Object> roles = authentication.getAuthorities().stream().collect(Collectors.toMap(e->"ROLE", GrantedAuthority::getAuthority));
        return Jwts.builder()
            .setClaims(Jwts.claims(roles))
            .setSubject(email)
            .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(TOKEN_EXPIRATION)))
            .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
            .compact();
    }

    public void issue(HttpServletResponse response, String token) throws IOException {
        response.addHeader(AUTHORIZATION, String.format("%s %s", BEARER, token));
        response.getWriter().write(String.format("%s %s", BEARER, token));
        response.flushBuffer();
    }

    public String resolve(HttpServletRequest request) {
        Enumeration<String> authorizations = request.getHeaders(AUTHORIZATION);
        while (authorizations.hasMoreElements()) {
            String authorization = authorizations.nextElement();
            if (authorization.toLowerCase().startsWith(BEARER.toLowerCase())) {
                return authorization.substring(BEARER.length()).trim();
            }
        }
        return Strings.EMPTY;
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> jws = this.parse(token);
        return new UsernamePasswordAuthenticationToken(jws.getBody().getSubject(), null, List.of(new SimpleGrantedAuthority(jws.getBody().get("ROLE").toString())));
    }

    public String getId(String token) {
        Jws<Claims> jws = this.parse(token);
        return jws.getBody().getSubject();
    }
}
