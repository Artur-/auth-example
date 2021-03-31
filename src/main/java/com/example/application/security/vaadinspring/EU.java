package com.example.application.security.vaadinspring;

import com.vaadin.flow.server.connect.EndpointUtil;
import com.vaadin.flow.server.connect.VaadinConnectController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// FIXME Remove
@Component
public class EU extends EndpointUtil {

    @Autowired
    public VaadinConnectController c;
}
