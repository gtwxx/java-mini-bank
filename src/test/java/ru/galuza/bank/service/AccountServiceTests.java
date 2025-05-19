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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountServiceTests {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void cleanDatabase() {
        accountRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void testGetClientAccounts() {
        Client client = clientRepository.save(new Client("user4"));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("A1", Currency.USD, BigDecimal.ZERO));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("A2", Currency.USD, BigDecimal.ZERO));

        List<Account> accounts = accountRepository.findAllAccountsByClientLogin(client.getLogin());
        assertEquals(2, accounts.size());
    }

    @Test
    void testSuccessfulTransfer() {
        Client client = clientRepository.save(new Client("user5"));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("F1", Currency.USD, BigDecimal.valueOf(500)));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("F2", Currency.USD, BigDecimal.ZERO));

        accountService.transfer("user5", "F1", "F2", BigDecimal.valueOf(200));

        Account from = accountRepository.findAccountByAccountNumber("F1");
        Account to = accountRepository.findAccountByAccountNumber("F2");

        assertEquals(0, from.getBalance().compareTo(BigDecimal.valueOf(300)));
        assertEquals(0, to.getBalance().compareTo(BigDecimal.valueOf(200)));
    }

    @Test
    void testTransferToSameAccountFails() {
        Client client = clientRepository.save(new Client("user6"));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("X1", Currency.USD, BigDecimal.valueOf(100)));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer("user6", "X1", "X1", BigDecimal.valueOf(10)));
    }

    @Test
    void testTransferDifferentCurrenciesFails() {
        Client client = clientRepository.save(new Client("user7"));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("USD1", Currency.USD, BigDecimal.valueOf(100)));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("EUR1", Currency.EUR, BigDecimal.ZERO));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer("user7", "USD1", "EUR1", BigDecimal.valueOf(10)));
    }

    @Test
    void testTransferFromForeignAccountFails() {
        Client owner = clientRepository.save(new Client("owner"));
        Client attacker = clientRepository.save(new Client("hacker"));

        clientService.createAccount(owner.getLogin(), new CreateAccountDTO("OWN1", Currency.USD, BigDecimal.valueOf(100)));
        clientService.createAccount(attacker.getLogin(), new CreateAccountDTO("HACK1", Currency.USD, BigDecimal.ZERO));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer("hacker", "OWN1", "HACK1", BigDecimal.valueOf(10)));
    }

    @Test
    void testTransferInsufficientFundsFails() {
        Client client = clientRepository.save(new Client("user8"));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("SRC", Currency.USD, BigDecimal.valueOf(50)));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("DST", Currency.USD, BigDecimal.ZERO));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer("user8", "SRC", "DST", BigDecimal.valueOf(100)));
    }

    @Test
    void testTransferToNonexistentAccountFails() {
        Client client = clientRepository.save(new Client("user9"));
        clientService.createAccount(client.getLogin(), new CreateAccountDTO("FROM", Currency.USD, BigDecimal.valueOf(50)));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer("user9", "FROM", "UNKNOWN", BigDecimal.valueOf(10)));
    }
}
