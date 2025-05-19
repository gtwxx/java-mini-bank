package ru.galuza.bank.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransferDTO(
        @NotBlank String toAccountNumber,
        @Positive BigDecimal amount
) {}

