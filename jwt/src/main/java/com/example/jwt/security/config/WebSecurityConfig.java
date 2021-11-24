package com.example.jwt.security.config;

import lombok.RequiredArgsConstructor;

import com.example.jwt.security.filter.DefaultAuthenticationFilter;
import com.example.jwt.security.filter.JwtAuthenticationFilter;
import com.example.jwt.security.service.DefaultUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DefaultUserDetailsService userDetailsService;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable(); // h2-console
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**","/js/**", "/h2-console/**", "/login**", "/error**", "/h2-console").permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.GET, "/users").hasRole("USER")
                .anyRequest().authenticated();
        http.addFilter(defaultAuthenticationFilter())
            .addFilterBefore(jwtAuthenticationFilter(), DefaultAuthenticationFilter.class);
        http.oauth2Login()
                .userInfoEndpoint().userService(oAuth2UserService);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DefaultAuthenticationFilter defaultAuthenticationFilter() throws Exception {
        return new DefaultAuthenticationFilter(this.authenticationManager());
    }
    
}