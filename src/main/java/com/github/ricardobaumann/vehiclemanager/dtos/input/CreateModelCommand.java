package com.github.ricardobaumann.vehiclemanager.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateModelCommand(
        @NotNull UUID brandId,
        @NotBlank @Size(min = 5, max = 100) String name,
        @NotNull @Positive BigDecimal price
) {
}
