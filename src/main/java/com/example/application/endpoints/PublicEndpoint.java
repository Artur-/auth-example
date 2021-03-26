package com.example.application.endpoints;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;

import org.joda.time.LocalDateTime;

@Endpoint
@AnonymousAllowed
public class PublicEndpoint {

    public String getServerTime() {
        return LocalDateTime.now().toString();
    }
}
