package ru.galuza.bank.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.galuza.bank.dto.TransferDTO;
import ru.galuza.bank.service.AccountService;
import ru.galuza.bank.service.ClientService;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final ClientService clientService;

    @GetMapping("clients/{login}/accounts")
    public ResponseEntity<?> getAllAccounts(@PathVariable String login) {
        if (!clientService.clientExistsByLogin(login)) {
            return ResponseEntity.badRequest().body(new IllegalArgumentException("No such user"));
        }
        return ResponseEntity.ok(accountService.getAllAccountsByClientLogin(login));
    }

    @PostMapping("clients/{clientLogin}/accounts/{fromAccountLogin}/transfer")
    public ResponseEntity<?> transfer(@PathVariable String clientLogin,
                                   @PathVariable String fromAccountLogin,
                                   @RequestBody @Valid TransferDTO transferDTO,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            accountService.transfer(
                    clientLogin,
                    fromAccountLogin,
                    transferDTO.toAccountNumber(),
                    transferDTO.amount()
            );
            return ResponseEntity.ok("Successfully transacted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
