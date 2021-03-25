package com.example.application.security;

import java.util.Collections;

import com.example.application.DataGenerator;
import com.example.application.security.vaadinspring.VaadinWebSecurityConfigurerAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(new User(DataGenerator.HARDCODED_USER1, "{noop}" + DataGenerator.HARDCODED_USER1,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_user"))));
        manager.createUser(new User(DataGenerator.HARDCODED_USER2, "{noop}" + DataGenerator.HARDCODED_USER2,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_admin"))));
        return manager;
    }

}