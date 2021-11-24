package com.example.jwt.security.util;

import javax.transaction.Transactional;

import com.example.jwt.security.service.DefaultUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements
  ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private DefaultUserDetailsService defaultUserDetailsService;
 
 
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
 
      if (alreadySetup)
          return;

      defaultUserDetailsService.createAdminIfNotFound();

      alreadySetup = true;
    }

}