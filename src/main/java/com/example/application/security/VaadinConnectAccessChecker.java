package com.example.application.security;


import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.server.VaadinService;

/**
 * Based on {@link com.vaadin.flow.server.connect.auth.VaadinConnectAccessChecker} with some modifications:
 *
 * 1. Some unused methods have been removed.
 * 2. Some methods that took a {@link Method} as argument now take a {@link Class}, these are commented with
 * "Modified for Class".
 */
public class VaadinConnectAccessChecker {

    private boolean xsrfProtectionEnabled = true;

    /**
     * Modified for class
     */
    public String check (Class<?> target, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            return verifyAuthenticatedUser(target, request);
        } else {
            return verifyAnonymousUser(target, request);
        }
    }

    /**
     * Modified for class
     */
    private String verifyAnonymousUser(Class<?> target,
                                       HttpServletRequest request) {
        if (!target.isAnnotationPresent(AnonymousAllowed.class)
                || cannotAccessClass(target, request)) {
            return "Anonymous access is not allowed";
        }
        return null;
    }

    /**
     * Modified for class
     */
    private String verifyAuthenticatedUser(Class<?> target,
                                           HttpServletRequest request) {
        if (cannotAccessClass(target, request)) {
            VaadinService vaadinService = VaadinService.getCurrent();
            final String accessDeniedMessage;
            if (vaadinService != null && !vaadinService
                    .getDeploymentConfiguration().isProductionMode()) {
                // suggest access control annotations in dev mode
                accessDeniedMessage = "Unauthorized access to Vaadin endpoint; "
                        + "to enable endpoint access use one of the following "
                        + "annotations: @AnonymousAllowed, @PermitAll, "
                        + "@RolesAllowed";
            } else {
                accessDeniedMessage = "Unauthorized access to Vaadin endpoint";
            }
            return accessDeniedMessage;
        }
        return null;
    }

    /**
     * Modified for class
     */
    private boolean cannotAccessClass(Class<?> target, HttpServletRequest request) {
        return requestForbidden(request) || !entityAllowed(target, request);
    }

    private boolean requestForbidden(HttpServletRequest request) {
        if (!xsrfProtectionEnabled) {
            return false;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        String csrfTokenInSession = (String) session
                .getAttribute(VaadinService.getCsrfTokenAttributeName());
        if (csrfTokenInSession == null) {
            if (getLogger().isInfoEnabled()) {
                getLogger().info(
                        "Unable to verify CSRF token for endpoint request, got null token in session");
            }

            return true;
        }

        String csrfTokenInRequest = request.getHeader("X-CSRF-Token");
        if (csrfTokenInRequest == null || !MessageDigest.isEqual(
                csrfTokenInSession.getBytes(StandardCharsets.UTF_8),
                csrfTokenInRequest.getBytes(StandardCharsets.UTF_8))) {
            if (getLogger().isInfoEnabled()) {
                getLogger().info("Invalid CSRF token in endpoint request");
            }

            return true;
        }

        return false;
    }

    private boolean entityAllowed(AnnotatedElement entity,
                                  HttpServletRequest request) {
        if (entity.isAnnotationPresent(DenyAll.class)) {
            return false;
        }
        if (entity.isAnnotationPresent(AnonymousAllowed.class)) {
            return true;
        }
        RolesAllowed rolesAllowed = entity.getAnnotation(RolesAllowed.class);
        if (rolesAllowed == null) {
            return entity.isAnnotationPresent(PermitAll.class);
        } else {
            return roleAllowed(rolesAllowed, request);
        }
    }

    private boolean roleAllowed(RolesAllowed rolesAllowed,
                                HttpServletRequest request) {
        for (String role : rolesAllowed.value()) {
            if (request.isUserInRole(role)) {
                return true;
            }
        }

        return false;
    }

    public void enableCsrf(boolean xsrfProtectionEnabled) {
        this.xsrfProtectionEnabled = xsrfProtectionEnabled;
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(VaadinConnectAccessChecker.class);
    }
}