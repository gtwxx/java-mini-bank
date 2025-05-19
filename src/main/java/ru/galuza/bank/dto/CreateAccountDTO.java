package ru.galuza.bank.dto;

import jakarta.validation.constraints.*;
import ru.galuza.bank.domain.Currency;

import java.math.BigDecimal;

public record CreateAccountDTO(
        @NotBlank String accountNumber,
        @NotNull Currency currency,
        @Positive BigDecimal balance
) {}

