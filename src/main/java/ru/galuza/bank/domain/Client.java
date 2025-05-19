package ru.galuza.bank.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        indexes = {@Index(columnList = "login", unique = true)}
)
public class Client {
    @Id
    @GeneratedValue
    private Long id;

    private String login;

    public Client(String login) {
        this.login = login;
    }
}
