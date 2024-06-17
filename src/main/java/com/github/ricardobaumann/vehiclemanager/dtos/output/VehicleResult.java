package com.github.ricardobaumann.vehiclemanager.dtos.output;

import com.github.ricardobaumann.vehiclemanager.entities.FuelType;

import java.util.UUID;

public record VehicleResult(
        UUID id,
        ModelResult model,
        Integer year,
        FuelType fuelType,
        Integer doorsAmount,
        String color
) {
}
