package com.example.jwt.security.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        Map<String, Object> attributes = new DefaultOAuth2UserService().loadUser(userRequest).getAttributes();
        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("OAUTH :: attributes({})", attributes);
        log.info("OAUTH :: nameAttributeKey({})", nameAttributeKey);
        log.info("OAUTH :: registrationId({})", registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("TEST_GUEST")),
                attributes,
                nameAttributeKey);
    }
}