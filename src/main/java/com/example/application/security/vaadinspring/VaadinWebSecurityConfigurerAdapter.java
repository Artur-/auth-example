package com.example.application.security.vaadinspring;

import com.example.application.security.vaadin.VaadinFramework;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

public abstract class VaadinWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/";

    /**
     * The paths listed as "ignoring" in this method are handled without any Spring
     * Security involvement. They have no access to any security context etc.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(VaadinFramework.getPublicResources());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Vaadin has its own CSRF protection.
        // Spring CSRF is not compatible with Vaadin internal requests
        http.csrf().ignoringRequestMatchers(VaadinFramework::isFrameworkInternalRequest);
        // nor with endpoints
        http.csrf().ignoringAntMatchers("/connect/**");

        // Ensure automated requests to e.g. closing push channels, service workers,
        // endpoints
        // are not counted as valid targets to redirect user to on login
        http.requestCache().requestCache(new NoInternalFrameworkRequestCache());

        // Restrict access to the application
        configureURLAccess(http.authorizeRequests(), http);

        configureLogin(http);
    }

    protected void configureURLAccess(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry,
            HttpSecurity http) throws Exception {
        // Vaadin internal requests must always be allowed and also need the security
        // context so they are here and not listed as static resources
        urlRegistry.requestMatchers(VaadinFramework::isFrameworkInternalRequest).permitAll();
        urlRegistry.antMatchers(VaadinFramework.getPublicResourcesRequiringSecurityContext()).permitAll();

        // Allow other requests only by authenticated users
        urlRegistry.anyRequest().authenticated();
    }

    /**
     * Configures how login should be handled in the application. By default sets up
     * a Spring form based login.
     * 
     * TODO: Default to Vaadin login
     * 
     * @throws Exception
     */
    protected void configureLogin(HttpSecurity http) throws Exception {
        // FormLoginConfigurer<HttpSecurity> formLogin =
        http.formLogin();
        // formLogin.loginPage(LOGIN_URL).permitAll();
        // formLogin.loginProcessingUrl(LOGIN_PROCESSING_URL);
        // formLogin.failureUrl(LOGIN_FAILURE_URL);
        // http.httpBasic();

        http.logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);

    }

}
