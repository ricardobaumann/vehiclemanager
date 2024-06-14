package com.github.ricardobaumann.vehiclemanager;

import com.github.ricardobaumann.vehiclemanager.entities.Brand;
import com.github.ricardobaumann.vehiclemanager.entities.Model;
import com.github.ricardobaumann.vehiclemanager.repos.BrandRepo;
import com.github.ricardobaumann.vehiclemanager.repos.ModelRepo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class TestObjects {
    private final BrandRepo brandRepo;
    private final ModelRepo modelRepo;

    public TestObjects(BrandRepo brandRepo, ModelRepo modelRepo) {
        this.brandRepo = brandRepo;
        this.modelRepo = modelRepo;
    }

    public Model createTestModel() {
        Brand brand = brandRepo.save(new Brand(UUID.randomUUID(), "brandfoo"));
        return modelRepo.save(new Model(UUID.randomUUID(), brand, "testing", new BigDecimal("100.99")));
    }

}
