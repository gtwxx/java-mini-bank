package ru.galuza.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.galuza.bank.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByLogin(String login);

    boolean existsByLogin(String login);
}
