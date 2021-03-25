package com.example.application.security.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.flow.server.HandlerHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;

public class VaadinFramework {

    public static List<String> getStaticResources() {
        List<String> staticResources = new ArrayList<>();
        staticResources.add("/VAADIN/**");
        staticResources.add("/favicon.ico");
        staticResources.add("/robots.txt");
        staticResources.add("/manifest.webmanifest");
        staticResources.add("/sw.js");
        staticResources.add("/offline-page.html");
        return staticResources;
    }

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        String path = request.getPathInfo();
        if (path == null || path.isEmpty()) {
            final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
            return parameterValue != null
                    && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
        }
        return false;
    }
}
