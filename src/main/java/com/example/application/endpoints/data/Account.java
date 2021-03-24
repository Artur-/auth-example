package com.example.application.endpoints.data;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
    @Id
    private UUID id;
    private String owner;
    private BigDecimal balance;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
