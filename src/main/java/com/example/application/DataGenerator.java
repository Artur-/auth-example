package com.example.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.example.application.endpoints.data.Account;
import com.example.application.endpoints.data.AccountRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@SpringComponent
public class DataGenerator {

    private static final LocalDateTime REF_TIME = LocalDateTime.of(2021, 1, 11, 0, 0, 0);

    public static final String HARDCODED_USER1 = "John";
    public static final String HARDCODED_USER2 = "Bianca";

    @Bean
    public CommandLineRunner loadData(AccountRepository accountRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (accountRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 10 Account entities...");
            ExampleDataGenerator<Account> accountGenerator = new ExampleDataGenerator<>(Account.class, REF_TIME);
            accountGenerator.setData(Account::setId, DataType.UUID);
            accountGenerator.setData(Account::setOwner, DataType.FIRST_NAME);

            List<Account> accounts = accountGenerator.create(10, seed);
            Random random = new Random();
            for (Account a : accounts) {
                a.setBalance(BigDecimal.valueOf(random.nextDouble() * 10000.0));
            }
            accounts.get(0).setOwner(HARDCODED_USER1);
            accounts.get(1).setOwner(HARDCODED_USER2);
            accountRepository.saveAll(accounts);

            logger.info("Generated demo data");
        };
    }

}