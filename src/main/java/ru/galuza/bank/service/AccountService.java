package ru.galuza.bank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.galuza.bank.domain.Account;
import ru.galuza.bank.domain.Client;
import ru.galuza.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientService clientService;

    public List<Account> getAllAccountsByClientLogin(String login) {
        return accountRepository.findAllAccountsByClientLogin(login);
    }

    @Transactional
    public synchronized void transfer(String clientLogin, String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (Objects.equals(fromAccountNumber, toAccountNumber)) {
            throw new IllegalArgumentException("Source and destination are equal");
        }

        Client fromClient = clientService.findByLogin(clientLogin);

        if (Objects.isNull(fromClient)) {
            throw new IllegalArgumentException("From client not found");
        }

        Account from = accountRepository.findByAccountNumberForUpdate(fromAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));

        if (!Objects.equals(from.getClient(), fromClient)) {
            throw new IllegalArgumentException("You are not owner of the money!!");
        }

        Account to = accountRepository.findByAccountNumberForUpdate(toAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        if (!Objects.equals(from.getCurrency(), to.getCurrency())) {
            throw new IllegalArgumentException("Transaction can't be done in different currencies");
        }

        from.withdraw(amount);
        accountRepository.save(from);
        to.deposit(amount);
        accountRepository.save(to);
    }
}
