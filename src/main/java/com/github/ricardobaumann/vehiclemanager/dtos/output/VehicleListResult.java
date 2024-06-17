package com.github.ricardobaumann.vehiclemanager.dtos.output;

import com.github.ricardobaumann.vehiclemanager.entities.FuelType;

import java.util.UUID;

public record VehicleListResult(
        UUID id,
        VehicleModel model,
        Integer year,
        FuelType fuelType,
        Integer doorsAmount,
        String color
) {
    public record VehicleModel(
            UUID id,
            String name
    ) {
    }
}
