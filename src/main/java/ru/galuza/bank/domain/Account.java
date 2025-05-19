package ru.galuza.bank.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table(
        indexes = {@Index(columnList = "accountNumber", unique = true)}
)
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String accountNumber;

    private Currency currency;

    private BigDecimal balance;

    public Account(Client client, String accountNumber, Currency currency, BigDecimal balance) {
        this.client = client;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
    }

    public synchronized void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public synchronized void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Low money(");
        }
        this.balance = this.balance.subtract(amount);
    }
}
