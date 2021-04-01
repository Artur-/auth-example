package com.example.application.views;

import java.math.BigDecimal;

import com.example.application.security.vaadinspring.SecurityUtils;
import com.example.application.services.BankService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route(value = "private/java")
@PageTitle("Private Java")
@RolesAllowed("user")
public class PrivateJavaView extends VerticalLayout {

    private BankService bankService;
    private Span balanceSpan = new Span();
    private SecurityUtils utils;

    public PrivateJavaView(BankService bankService, SecurityUtils utils) {
        this.bankService = bankService;
        this.utils = utils;

        updateBalanceText();
        add(balanceSpan);
        add(new Button("Apply for a loan", this::applyForLoan));
    }

    private void updateBalanceText() {
        if (utils.getAuthenticatedUser() == null) {
            throw new IllegalStateException("Unauthenticated user should not be able to open this view");
        }
        String name = utils.getAuthenticatedUser().getUsername();
        BigDecimal balance = bankService.getBalance();
        this.balanceSpan.setText(String.format("Hello %s, your bank account balance is $%s.", name, balance));

    }

    private void applyForLoan(ClickEvent<Button> e) {
        bankService.applyForLoan();
        updateBalanceText();
    }
}
