package ru.galuza.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClientDTO(
        @NotBlank(message = "Client login must be")
        @Size(min = 5, max = 20)
        String login
) {}
