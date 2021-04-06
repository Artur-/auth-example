package com.example.application;

import com.example.application.security.VaadinConnectAccessChecker;
import com.example.application.security.vaadinspring.SecurityUtils;
import com.example.application.views.PublicJavaView;
import com.vaadin.flow.component.internal.JavaScriptBootstrapUI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.UIInitEvent;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class ServiceInitListener implements VaadinServiceInitListener, UIInitListener, BeforeEnterListener {

    private final VaadinConnectAccessChecker vaadinConnectAccessChecker = new VaadinConnectAccessChecker();
    private final SecurityUtils securityUtils;

    public ServiceInitListener(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
        vaadinConnectAccessChecker.enableCsrf(false);
    }

    // TODO: If we use DenyAll by default, how do we configure navigation targets like these?
    private static final Class<?>[] NAVIGATION_TARGET_ALLOW_LIST = new Class<?>[]{
            RouteNotFoundError.class,
            JavaScriptBootstrapUI.ClientViewPlaceholder.class
    };

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(this);
    }

    @Override
    public void uiInit(UIInitEvent uiInitEvent) {
        uiInitEvent.getUI().addBeforeEnterListener(this);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (Arrays.stream(NAVIGATION_TARGET_ALLOW_LIST)
                .anyMatch(target -> target.isAssignableFrom(beforeEnterEvent.getNavigationTarget()))) {
            return;
        }
        VaadinServletRequest vaadinServletRequest = (VaadinServletRequest) VaadinRequest.getCurrent();
        HttpServletRequest httpServletRequest = vaadinServletRequest.getHttpServletRequest();

        String error = vaadinConnectAccessChecker.check(beforeEnterEvent.getNavigationTarget(), httpServletRequest);
        if (error != null) {
            if (securityUtils.getAuthenticatedUser() == null) {
                // TODO: Forward or reroute to login (ideally supporting being forward back to the requested URL after logging in)
                beforeEnterEvent.forwardTo(PublicJavaView.class);
            } else {
                beforeEnterEvent.rerouteToError(AccessDeniedException.class);
            }
        }
    }
}
