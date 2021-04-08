package com.example.application.security;

import java.util.Collections;

import com.example.application.DataGenerator;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/";

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(new User(DataGenerator.HARDCODED_USER1, "{noop}" + DataGenerator.HARDCODED_USER1,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_user"))));
        manager.createUser(new User(DataGenerator.HARDCODED_USER2, "{noop}" + DataGenerator.HARDCODED_USER2,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_admin"))));
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http
                .authorizeRequests();
        urlRegistry.requestMatchers(new AntPathRequestMatcher("/")).permitAll();
        urlRegistry.requestMatchers(new AntPathRequestMatcher("/public/**")).permitAll();
        // FIXME This should not be needed. I already annotated it with
        // @AnonymousAllowed (also the URL is an internal detail)
        urlRegistry.requestMatchers(new AntPathRequestMatcher("/connect/PublicEndpoint/getServerTime")).permitAll();

        // "/secret" is protected by the default rule to allow access only for
        // authenticated users and does not need to be listed here
        urlRegistry.requestMatchers(new AntPathRequestMatcher("/admin-only/**")).hasRole("admin");

        super.configure(http);

        // FormLoginConfigurer<HttpSecurity> formLogin =
        http.formLogin();
        // formLogin.loginPage(LOGIN_URL).permitAll();
        // formLogin.loginProcessingUrl(LOGIN_PROCESSING_URL);
        // formLogin.failureUrl(LOGIN_FAILURE_URL);
        // http.httpBasic();

        http.logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        // Allow public access to everything inside /images
        web.ignoring().antMatchers("/images/*");
    }

}
