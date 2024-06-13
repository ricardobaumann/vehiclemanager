package com.github.ricardobaumann.vehiclemanager.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBrandCommand(
        @NotBlank @Size(min = 5, max = 100) String name
) {
}
