package com.github.ricardobaumann.vehiclemanager.dtos.output;

import java.math.BigDecimal;
import java.util.UUID;

public record ModelResult(
        UUID id,
        BrandResult brand,
        String name,
        BigDecimal price
) {
}
