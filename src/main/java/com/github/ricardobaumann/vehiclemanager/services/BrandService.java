package com.github.ricardobaumann.vehiclemanager.services;

import com.github.ricardobaumann.vehiclemanager.dtos.input.CreateBrandCommand;
import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class BrandService {

    public Brand create(CreateBrandCommand createBrandCommand) {
        Brand brand = new Brand();
        brand.setId(UUID.randomUUID());
        return brand;
    }

}
