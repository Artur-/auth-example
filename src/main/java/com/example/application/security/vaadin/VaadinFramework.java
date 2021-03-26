package com.example.application.security.vaadin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.flow.server.HandlerHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;

public class VaadinFramework {

    public static String[] getPublicResources() {
        return new String[] { //
                "/favicon.ico", //
                "/robots.txt", //
                "/manifest.webmanifest", //
                "/sw.js", //
                "/offline-page.html", //
        };
    }

    public static String[] getPublicResourcesRequiringSecurityContext() {
        return new String[] { //
                "/VAADIN/**", //
        };
    }

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        String path = getRequestPath(request);
        // TODO Push close message unless covered already
        if (path.isEmpty() || "/".equals(path)) {
            final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
            return parameterValue != null
                    && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
        }

        return false;
    }

    private static String getRequestPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return "";
        }
        return pathInfo;
    }

    public static boolean isEndpointRequest(HttpServletRequest request) {
        String path = getRequestPath(request);
        return path.startsWith("/connect/");
    }

}
