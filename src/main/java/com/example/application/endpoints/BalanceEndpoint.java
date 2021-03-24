package com.example.application.endpoints;

import java.math.BigDecimal;

import javax.annotation.security.RolesAllowed;

import com.example.application.services.BankService;
import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;

import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
// @RolesAllowed("user")
@AnonymousAllowed
public class BalanceEndpoint {

    @Autowired
    private BankService bankService;

    public BigDecimal getBalance() {
        return bankService.getBalance();
    }
}
