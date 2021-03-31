package com.example.application.security.vaadinspring;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.server.connect.EndpointUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

public abstract class VaadinWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/";

    @Autowired
    private NoInternalVaadinRequestsCache requestCache;

    @Autowired
    private EU endpointUtil;
    @Autowired
    private RequestUtil requestUtil;

    /**
     * The paths listed as "ignoring" in this method are handled without any Spring
     * Security involvement. They have no access to any security context etc.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HandlerHelper.getPublicResources());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Vaadin has its own CSRF protection.
        // Spring CSRF is not compatible with Vaadin internal requests
        http.csrf().ignoringRequestMatchers(requestUtil::isFrameworkInternalRequest);
        // nor with endpoints
        http.csrf().ignoringRequestMatchers(endpointUtil::isEndpointRequest);

        // Ensure automated requests to e.g. closing push channels, service workers,
        // endpoints are not counted as valid targets to redirect user to on login
        http.requestCache().requestCache(requestCache);

        // Restrict access to the application
        configureURLAccess(http.authorizeRequests(), http);

        configureLogin(http);
    }

    protected void configureURLAccess(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry,
            HttpSecurity http) throws Exception {
        // Vaadin internal requests must always be allowed and also need the security
        // context so they are here and not listed as static resources
        urlRegistry.requestMatchers(requestUtil::isFrameworkInternalRequest).permitAll();
        urlRegistry.antMatchers(HandlerHelper.getPublicResourcesRequiringSecurityContext()).permitAll();

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
