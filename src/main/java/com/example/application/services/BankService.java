package com.example.application.services;

import java.math.BigDecimal;
import java.util.Optional;

import com.example.application.endpoints.data.Account;
import com.example.application.endpoints.data.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepository;

    public void applyForLoan() {
        String name = userService.getName();
        Optional<Account> acc = accountRepository.findByOwner(name);
        if (!acc.isPresent()) {
            return;
        }
        Account account = acc.get();
        account.setBalance(account.getBalance().add(new BigDecimal("10000")));
        accountRepository.save(account);
    }

    public BigDecimal getBalance() {
        String name = userService.getName();
        return accountRepository.findByOwner(name).map(Account::getBalance).orElse(null);
    }
}
