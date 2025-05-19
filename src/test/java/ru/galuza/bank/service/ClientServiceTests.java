package ru.galuza.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.galuza.bank.domain.Account;
import ru.galuza.bank.domain.Client;
import ru.galuza.bank.domain.Currency;
import ru.galuza.bank.dto.CreateAccountDTO;
import ru.galuza.bank.repository.AccountRepository;
import ru.galuza.bank.repository.ClientRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClientServiceTests {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;


    @BeforeEach
    void cleanDatabase() {
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void testCreateClient() {
        Client client = clientRepository.save(new Client("user1"));
        assertNotNull(client.getId());
        assertEquals("user1", client.getLogin());
    }

    @Test
    void testCreateAccountForClient() {
        Client client = clientRepository.save(new Client("user2"));
        CreateAccountDTO request = new CreateAccountDTO("ACC123", Currency.USD, BigDecimal.valueOf(1000));
        Account account = clientService.createAccount(client.getLogin(), request);

        assertNotNull(account.getId());
        assertEquals("ACC123", account.getAccountNumber());
        assertEquals(Currency.USD, account.getCurrency());
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
        assertEquals(client.getId(), account.getClient().getId());
    }

    @Test
    void testAccountNumberUniqueness() {
        Client client = clientRepository.save(new Client("user3"));
        CreateAccountDTO request = new CreateAccountDTO("ACC999", Currency.EUR, BigDecimal.ZERO);
        clientService.createAccount(client.getLogin(), request);

        assertThrows(IllegalArgumentException.class, () -> clientService.createAccount(client.getLogin(), request));
    }

}
