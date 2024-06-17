package com.github.ricardobaumann.vehiclemanager.dtos.input;

import com.github.ricardobaumann.vehiclemanager.entities.FuelType;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateVehicleCommand(
        @NotNull UUID modelId,
        @NotNull @Positive Integer year,
        @NotNull FuelType fuelType,
        @NotNull @PositiveOrZero Integer doorsAmount,
        @NotBlank @Size(max = 100) String color
) {
}
