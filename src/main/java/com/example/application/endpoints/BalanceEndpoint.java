package com.example.application.endpoints;

import java.math.BigDecimal;

import javax.annotation.security.PermitAll;

import com.example.application.services.BankService;
import com.vaadin.flow.server.connect.Endpoint;

import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
@PermitAll
public class BalanceEndpoint {

    @Autowired
    private BankService bankService;

    public BigDecimal getBalance() {
        return bankService.getBalance();
    }

    public void applyForLoan() {
        bankService.applyForLoan();
    }
}
