package ru.galuza.bank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.galuza.bank.domain.Account;
import ru.galuza.bank.domain.Client;
import ru.galuza.bank.dto.CreateAccountDTO;
import ru.galuza.bank.repository.AccountRepository;
import ru.galuza.bank.repository.ClientRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    public Client createClient(String login) {
        if (clientRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Login already used");
        }

        Client client = new Client(login);

        clientRepository.save(client);

        return client;
    }

    @Transactional
    public Account createAccount(String clientLogin, CreateAccountDTO accountDTO) {
        Client client = clientRepository.findByLogin(clientLogin);

        if (Objects.isNull(client)) {
            throw new IllegalArgumentException("Client must be not null");
        }

        if (accountRepository.existsAccountByAccountNumber(accountDTO.accountNumber())) {
            throw new IllegalArgumentException("Account name already used");
        }

        Account account = new Account(client, accountDTO.accountNumber(), accountDTO.currency(), accountDTO.balance());
        accountRepository.save(account);

        return account;
    }

    public boolean clientExistsByLogin(String login) {
        return clientRepository.existsByLogin(login);
    }

    public Client findByLogin(String clientLogin) {
        return clientRepository.findByLogin(clientLogin);
    }
}
