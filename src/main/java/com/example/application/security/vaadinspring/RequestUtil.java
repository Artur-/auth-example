package com.example.application.security.vaadinspring;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.spring.VaadinConfigurationProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestUtil {

    @Autowired
    private VaadinConfigurationProperties configurationProperties;

    public boolean isFrameworkInternalRequest(HttpServletRequest request) {
        String vaadinMapping = configurationProperties.getUrlMapping();
        if (vaadinMapping.endsWith("/*")) {
            vaadinMapping = vaadinMapping.substring(0, vaadinMapping.length() - "/*".length());
        }
        return HandlerHelper.isFrameworkInternalRequest(vaadinMapping, request);
    }

}
