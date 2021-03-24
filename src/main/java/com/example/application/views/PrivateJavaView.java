package com.example.application.views;

import java.math.BigDecimal;

import com.example.application.services.BankService;
import com.example.application.services.UserService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "private-java")
@PageTitle("Private Java")
public class PrivateJavaView extends VerticalLayout {

    private BankService bankService;
    private Span balanceSpan = new Span();
    private UserService userService;

    public PrivateJavaView(UserService userService, BankService bankService) {
        this.bankService = bankService;
        this.userService = userService;

        updateBalanceText();
        add(balanceSpan);
        add(new Button("Apply for a loan", this::applyForLoan));
    }

    private void updateBalanceText() {
        String name = userService.getName();
        BigDecimal balance = bankService.getBalance();
        this.balanceSpan.setText(String.format("Hello %s, your bank account balance is $%s.", name, balance));

    }

    private void applyForLoan(ClickEvent<Button> e) {
        bankService.applyForLoan();
    }
}
