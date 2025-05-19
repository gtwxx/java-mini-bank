package ru.galuza.bank.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.galuza.bank.domain.Account;
import ru.galuza.bank.dto.CreateAccountDTO;
import ru.galuza.bank.dto.CreateClientDTO;
import ru.galuza.bank.service.ClientService;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/clients/new")
    public ResponseEntity<?> createClient(@Valid @RequestBody CreateClientDTO clientDTO,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        return ResponseEntity.ok(clientService.createClient(clientDTO.login()));
    }

    @PostMapping("clients/{login}/accounts/new")
    public ResponseEntity<?> createAccount(@PathVariable String login,
                                           @RequestBody @Valid CreateAccountDTO request,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            Account account = clientService.createAccount(login, request);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
